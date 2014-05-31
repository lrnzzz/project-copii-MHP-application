package com.adb.gfxexample;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.tv.xlet.Xlet;
import javax.tv.xlet.XletContext;
import javax.tv.xlet.XletStateChangeException;

import org.dvb.event.EventManager;
import org.dvb.event.UserEvent;
import org.dvb.event.UserEventListener;
import org.dvb.event.UserEventRepository;
import org.dvb.io.ixc.IxcRegistry;
import org.dvb.ui.DVBColor;

import tv.osmosys.telenet.ixc.NavigatorAPI;

import com.adb.app.gfx.backend.Transformable;
import com.adb.app.gfx.backend.Window;
import com.adb.app.gfx.backend.WindowManager;
import com.adb.app.gfx.backend.WindowManagerFactory;
import com.adb.app.gfx.backend.WindowPaintListener;
import com.adb.app.gfx.backend.transformation.Curve;
import com.adb.app.gfx.backend.transformation.Transformation;
import com.adb.app.gfx.backend.transformation.TransformationFactory;
import com.adb.gfxexample.utils.Delay;
import com.adb.gfxexample.utils.DoIt;
import com.adb.gfxexample.utils.Utils;
import com.adb.gfxexample.utils.VideoAndStillManager;

public class AdvancedDemoXlet implements Xlet, UserEventListener {

    public class CallbackAfterTransformation implements Runnable, DoIt {

    	Delay delay;
		private Runnable vScrollFinishCallback;
    	
		public void run() {
			if (gridShown)
			{
				if (delay == null)
				{
					delay = new Delay(400, true);
				}
				delay.use(this);
				if (vScrollFinishCallback != null)
				{
					vScrollFinishCallback.run();
					vScrollFinishCallback = null;
				}
			}
		}

		public void doIt() {
			Channel channel = channels[getSelectedChannel()];
			channel.setSelected(true);
			channel.setEventsPos(currentScrollPos);
			selectorGroup.setSelectedChannel(channel);
			manager.processEvents();			
		}
		
		public void stop()
		{
			if (delay != null)
			{
				delay.use(null);
			}
		}
		
		public void setVScrollCallback(Runnable v)
		{
			vScrollFinishCallback = v;
		}

	}

	private static final String[] CHANNELS_NAMES = new String[]{"2BE", "Acht", "AnimPlanetHD", "Be1", "DiscoveryHD", "VT4",
		"vijfTV", "eenHD", "NationalGeographicHD", "VTMHD",  "EuroSportHD", "Nickelodeon"};

	private static final Event[] EVENTS_NAME = new Event[]{new Event("Shark Nicole", "Documentaire.", "Wetenschappers Michael Scholl en Ramo Bonfil volgen een witte haai, om zijn gedrag in zijn natuurlijke omgeving te bestuderen.", null, 0, 300)
		, new Event("Mad Man", null, null, null, 0, 120)
		, new Event("The Escape Factory",null, null, null , 0, 220)
		, new Event("Fast & Furious 4", "FILM / ACTIE/DETECT./THRILLER", "Brian O'Conner is inmiddels opgeklommen tot FBI-agent en zit in een speciaal team dat achter de drugsbaron Braga aanzit",
				Utils.loadImage("img/events/F64A36F0E5284FFE_A500A61BF7484FC6.jpg") ,0, 400)
		, new Event("Grey's Anatomy", "Amerikaanse dramareeks.", "(jg 7/afl 9) Slow Night, So Lon",
				Utils.loadImage("img/events/1E09EE321FE643B7_88F5DCA5320F4549.jpg") ,0, 180)
		, new Event("Ninja Assassin", "FILM", "Als kind werd Raizo door de Ozunu Clan van de straat geplukt en getransformeerd tot een meedogenloze moordenaar.",
				Utils.loadImage("img/events/DCC80FD2F0A542F4_AAF9A0849F78DEC0.jpg") ,0, 200)
								};
	private static final String[] MENU_ITEMS_NAMES = new String[]{"instellingen", "tvtheek", "zoeken", "tvgids"
		, "apps", "help", "opnames", "tvshop"};
		
	WindowManager manager;

	private XletContext context;
	//private NavigatorAPI naviApi;
	private Channel[] channels;
	
//	private Row[] rows = new Row[8];
	
	private GridGroup upGrid;
	
	private int selectedChannel = 3;
	private NavigatorAPI naviApi;
	private CallbackAfterTransformation callbackAfterTransformation = new CallbackAfterTransformation();
	private SelectorGroup selectorGroup;
	private Transformable gridWindow;
	private int currentScrollPos;

	private boolean gridShown;

	private MenuItemRenderer[] menuItem = new MenuItemRenderer[MENU_ITEMS_NAMES.length];;

	private Transformable menuItemsGroup;

	private int selectedMenuItem = 3;

	private Transformable menuGroup;

	private boolean menuShown;

	public void initXlet(XletContext ctx) throws XletStateChangeException {
		this.context = ctx;
		
		channels = new Channel[CHANNELS_NAMES.length];
		for (int i = 0; i < channels.length; i++)
		{
			channels[i] = new Channel(CHANNELS_NAMES[i], i + 1,
					Utils.loadImage("img/ch_logos/active/16-9_W_" + CHANNELS_NAMES[i] + ".png"),
					Utils.loadImage("img/ch_logos/inactive/16-9_W_" + CHANNELS_NAMES[i] + ".png"),
					generateEvents(4000));
		}
		
	}
	
	private void blockNavi()
	{
		naviApi = readNavigatorAPI();

		if (naviApi != null) {
			hideDelay.use(null);
			try {
				naviApi.suppressChannelBanner(true);
				naviApi.suppressMessages(0xFF);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	Delay hideDelay = new Delay(5000);
	
	DoIt hideDoIt = new DoIt() {
		
		public void doIt() {
		if (naviApi != null) {
				try {
						naviApi.suppressChannelBanner(false);
						naviApi.suppressMessages(0);
					} catch (RemoteException e) {
						 //TODO Auto-generated catch block
						e.printStackTrace();
					}
				  }
		}
	};

	private Curve ctf; 
	
	private void unblockNavi()
	{
		hideDelay.use(hideDoIt);
	}
	
	private Event[] generateEvents(int length) {
		ArrayList result = new ArrayList(); 
		int currentLength = 0;
		while (currentLength < length)
		{
			int idx = (int) (Math.random() * EVENTS_NAME.length);
			Event ev = EVENTS_NAME[idx].getClone();
			ev.setStart(currentLength);
			result.add(ev);
			currentLength += ev.getDuration();
		}
		Event[] resultA = new Event[result.size()];
		result.toArray(resultA);
		return resultA;
	}

	public void startXlet() throws XletStateChangeException {
		WindowManager wm = WindowManagerFactory.get2DWindowManager();
		wm.initialize(new Dimension(1280, 720), new Dimension(1280, 720));
		manager = wm;
		
		prepareGrid();
		prepareMenu();

		Utils.waitForLoadAllImages();
		       
        UserEventRepository uer = new UserEventRepository("ArrowOk");
        uer.addAllArrowKeys();
        uer.addKey(KeyEvent.VK_ENTER);
        uer.addKey(KeyEvent.VK_CANCEL);
        uer.addKey(KeyEvent.VK_ESCAPE);
        uer.addKey(464);
        EventManager.getInstance().addUserEventListener(this, uer);
        
        Rectangle b = gridWindow.getBounds();
        b.y -= 720;
		gridWindow.setBounds(b);
		selectorGroup.moveY(720);
		
		gridWindow.show();
		selectorGroup.show();
		manager.processEvents();
		gridWindow.hide();
		selectorGroup.hide();
		manager.processEvents();
		
        b = gridWindow.getBounds();
        b.y += 720;
		gridWindow.setBounds(b);
		selectorGroup.moveY(-720);	
		
		
		
        showMenu();
        manager.processEvents();

	}

	private void showMenu() {
		if (!menuShown)
		{
			blockNavi();
			System.out.println("ShowMenu");
			menuGroup.show();
			final TransformationFactory f = manager.getTransformationFactory();
			Transformation trans = f.createTranslateTransformation(300, 0, 200, 0);
			menuGroup.applyTransformation(trans);
			manager.processEvents();
			menuShown = true;
		}
	}
	
	private void hideMenu() {
		if (menuShown)
		{
			System.out.println("HideMenu");
			final TransformationFactory f = manager.getTransformationFactory();
			Transformation trans = f.createAlphaTransformation(300, 0.0f);
			trans.setFinishCallback(new Runnable() {
				
				public void run() {
					menuGroup.resetTransformations();
					menuGroup.hide();
				}
			});
			menuGroup.applyTransformation(trans);
			manager.processEvents();
			menuShown = false;
		}
	}

	private void prepareMenu() {
		menuGroup = (Transformable) manager.requestWindow(manager.getRootWindow(),
				new Rectangle(0, -200, 1280, 200), WindowManager.WINDOW_FLAGS__CONTAINER_ONLY);		
		
		Window menuBackground = manager.requestWindow(menuGroup,
				new Rectangle(0, 0, 1280, 200), 0);
		menuBackground.setWindowPaintListener(new ImageRenderer("img/homeMenu_Background.png"));
		menuBackground.show();
		
		menuItemsGroup = (Transformable) manager.requestWindow(menuGroup,
				new Rectangle(0, 0, 1280, 200), WindowManager.WINDOW_FLAGS__CONTAINER_ONLY);
		
		for(int i = 0; i < MENU_ITEMS_NAMES.length; i++)
		{
			Window menuItemWindow = manager.requestWindow(menuItemsGroup,
					new Rectangle(75 + 220 * i - 220, 40, 225, 122), 0);
			MenuItemRenderer menuItemRenderer = new MenuItemRenderer((Transformable)menuItemWindow, "img/menu/inactive/" + MENU_ITEMS_NAMES[i]  + ".png",
					"img/menu/active/" + MENU_ITEMS_NAMES[i]  + ".png");
			if (i == selectedChannel)
			{
				menuItemRenderer.setSelected(1);
			}
			menuItem[i] = menuItemRenderer;
			menuItemWindow.setWindowPaintListener(menuItemRenderer);
			menuItemWindow.show();
		}
		
		
		Window menuFadeL = manager.requestWindow(menuGroup,
				new Rectangle(10, 0, 220, 160), 0);
		menuFadeL.setWindowPaintListener(new ImageRenderer("img/homeMenu_l_fade.png"));
		menuFadeL.show();
		
		Window menuFadeR = manager.requestWindow(menuGroup,
				new Rectangle(1280 - 220 - 10, 0, 220, 160), 0);
		menuFadeR.setWindowPaintListener(new ImageRenderer("img/homeMenu_r_fade.png"));
		menuFadeR.show();
		
		menuItemsGroup.show();		
	}

	private void hideGrid() {
		if (gridShown)
		{
			VideoAndStillManager.getInstance().hideStill();
			Rectangle videoRec = new Rectangle(0, 0, 1280, 720);
			VideoAndStillManager.getInstance().playVideo(videoRec);
			gridWindow.hide();
			selectorGroup.hide();
			manager.processEvents();
			gridShown = false;
		}
	}

	private void showGrid() {
		if (!gridShown)
		{
			VideoAndStillManager.getInstance().showStill("/img/bg.m2v");
			Rectangle videoRec = new Rectangle(980, 565, 300, 300 * 9 / 16);
			VideoAndStillManager.getInstance().playVideo(videoRec);
			gridWindow.show();
			selectorGroup.show();
			manager.processEvents();
			gridShown = true;
		}
	}

	private void prepareGrid() {
		Window selectorUp = manager.requestWindow(manager.getRootWindow(), new Rectangle(129, 144 + 3 * 54 , 1280 - 129 , 54), 0);
		selectorUp.setWindowPaintListener(new ImageRenderer("img/gid_bg_channel_focus_up.png"));
		
		
		gridWindow =  (Transformable) manager.requestWindow(manager.getRootWindow(),
				new Rectangle(129, 144 - 50, 1280 - 129 , 720 - 144), WindowManager.WINDOW_FLAGS__CONTAINER_ONLY);
			
		upGrid = new GridGroup(channels, manager, gridWindow);		
		
		Window selectorDown = manager.requestWindow(manager.getRootWindow(), new Rectangle(129, 144 + 3 * 54 , 1280 - 129 , 240), 0);
		SelectorRenderer selectorRenderer = new SelectorRenderer((Transformable) selectorDown);
		selectorDown.setWindowPaintListener(selectorRenderer);	
		
		selectorGroup = new SelectorGroup((Transformable)selectorUp, (Transformable)selectorDown, selectorRenderer);
					       
		//manager.processEvents();
		setSelectedChannel(3);
		channels[selectedChannel].setSelected(true);
		selectorGroup.setSelectedChannel(channels[selectedChannel]);
		//selectorDown.invalidate(selectorDown.getBounds());		
		
		// add clear video area
		Rectangle videoRec = new Rectangle(980 - 129, 565 - 144 + 50, 300, 300 * 9 / 16);
		Window videoWindow = manager.requestWindow(gridWindow, videoRec, WindowManager.WINDOW_FLAGS__BLEND_SRC);
		videoWindow.setWindowPaintListener(new WindowPaintListener() {	
			public void paintWindow(Graphics g, Rectangle invalidBounds) {
				g.setColor(new DVBColor(0,0,0,0));
				g.fillRect(invalidBounds.x, invalidBounds.y, invalidBounds.width, invalidBounds.height);
			}
		});
		videoWindow.show();
	}

	public void pauseXlet() {
		// TODO Auto-generated method stub
	}

	public void destroyXlet(boolean unconditional)
			throws XletStateChangeException {
		if (manager != null) {
			manager.deinitialize();
		}
		VideoAndStillManager.getInstance().hideStill();
		VideoAndStillManager.getInstance().playVideo(new Rectangle(0, 0, 1280, 720));
	}

	public void userEventReceived(UserEvent event) {
	  if (event.getType() == KeyEvent.KEY_PRESSED)
	  {
		final TransformationFactory f = manager.getTransformationFactory();
		Transformation trans = null;
		Transformation trans2 = null;
		Transformation trans3 = null;
	    switch(event.getCode())
	    {
	      case KeyEvent.VK_UP:
	    	  if(gridShown)
	    	  {
	    		  if (selectedChannel > 3)
	    		  {
	    			  trans = f.createScrollingTransformation(200, 0f, - upGrid.getRowFactor());
	    			  upGrid.applyTransformation(trans);
	    			  setSelectedChannel(getSelectedChannel() - 1);
	    		  }
	    		  else if(selectedChannel > 0 && selectedChannel <=3)
	    		  {
	    			  trans = f.createTranslateTransformation(200, 0, -54, 0);
	    			  selectorGroup.applyTransformation(trans);
	    			  setSelectedChannel(getSelectedChannel() - 1);
	    		  }
	    	  }
	    	  break;
	      case KeyEvent.VK_DOWN:
	    	  if(gridShown)
	    	  {
					if (selectedChannel >= 0 && selectedChannel < 3) {
						trans = f.createTranslateTransformation(200, 0, 54, 0);
						selectorGroup.applyTransformation(trans);
						setSelectedChannel(getSelectedChannel() + 1);
					} else if (selectedChannel < channels.length - 1) {
						trans = f.createScrollingTransformation(200, 0f,
								upGrid.getRowFactor());
						upGrid.applyTransformation(trans);
						setSelectedChannel(getSelectedChannel() + 1);
					}
			  }
	    	  break;
	      case KeyEvent.VK_RIGHT:
	    	  if(gridShown)
	    	  {
	    		  trans = createScrollTrans(f, 1);

	    		  upGrid.applyEventsTransformation(trans);
	    		  channels[selectedChannel].setSelected(false);
	    	  }
	    	  else if(menuShown)
	    	  {
	    		  trans = f.createTranslateTransformation(400, -220, 0, 0);
	    		  menuItemsGroup.applyTransformation(trans);
	    		  menuItem[selectedMenuItem].setSelected(0);
	    		  menuItem[selectedMenuItem].getWindow().invalidate(null);
	    		  selectedMenuItem++;
	    		  selectedMenuItem %=  menuItem.length;
	    		  menuItem[selectedMenuItem].setSelected(1);
	    		  menuItem[selectedMenuItem].getWindow().invalidate(null);
	    		  
	    		  Transformable mItem = menuItem[(selectedMenuItem - 4 + menuItem.length) % menuItem.length].getWindow();
	    		  Rectangle b = new Rectangle(mItem.getBounds());
	    		  b.x += menuItem.length * 220;
	    	  	  mItem.setBounds(b);
	    	  }
	    	  break;
	      case KeyEvent.VK_LEFT:
	    	  if(gridShown)
	    	  {
	    		  trans = createScrollTrans(f, -1);
	    		  upGrid.applyEventsTransformation(trans);
	    		  channels[selectedChannel].setSelected(false);
	    	  }
	    	  else if(menuShown)
	    	  {
	    		  trans = f.createTranslateTransformation(400, 220, 0, 0);
	    		  menuItemsGroup.applyTransformation(trans);
	    		  menuItem[selectedMenuItem].setSelected(0);
	    		  menuItem[selectedMenuItem].getWindow().invalidate(null);
	    		  selectedMenuItem = menuItem.length + selectedMenuItem - 1;
	    		  selectedMenuItem %=  menuItem.length;
	    		  menuItem[selectedMenuItem].setSelected(1);
	    		  menuItem[selectedMenuItem].getWindow().invalidate(null);
	    		  
	    		  Transformable mItem = menuItem[(selectedMenuItem + 5) % menuItem.length].getWindow();
	    		  Rectangle b = new Rectangle(mItem.getBounds());
	    		  b.x -= menuItem.length * 220;
	    	  	  mItem.setBounds(b);
	    	  }
	    	  break;
	      case KeyEvent.VK_ENTER:
	    	  if(menuShown)
	    	  {
	    		  hideMenu();
	    		  showGrid();
	    	  }
	    	  else if (!gridShown)
	    	  {
	    		  showMenu();
	    		  //showGrid();
	    	  }
	    	  break;
	      case KeyEvent.VK_CANCEL:
	      case KeyEvent.VK_ESCAPE:
	    	  hideGrid();
	    	  hideMenu();
	    	  unblockNavi();
	    	  break;
    	  // menu
	      case 464:
	    	  System.out.println("menuPressed");
	    	  showMenu();
	    	  break;
	    }
	    if (trans != null)
	    {
	    	if (trans != null)
	    	{
	    		if (ctf == null)
	    		{
	    			ctf = f.createCurveFromData(new float[]{
		              		0.0f, 0.0f,
		              	    0.28220585f, 0.0044380147f,
		              	    0.70173603f, 1.0014485f,
		              	    1.0f, 1.0f
		     			   }, 1);
	    		}
	    		trans.setTimelineCurve(ctf);
		     			   
	    		if (gridShown && selectorGroup != null)
	    		{
	    			selectorGroup.setSelectedChannel(null);
	    			callbackAfterTransformation.stop();
	    		}
    			trans.setFinishCallback(callbackAfterTransformation);
	    	}
		    manager.processEvents();	
	    }
	  }
	}

	private Transformation createScrollTrans(TransformationFactory f, int dir) {
	  Transformation trans = null;
    	  
	  int surfaceSize = upGrid.getEventsSurfaceWidth();
	  int scrollPos = currentScrollPos;
	  Channel channel = channels[getSelectedChannel()];
	  Event ev = null;
	  if (dir < 0)
	  {
		  ev = channel.getEventBefore(scrollPos);
	  }
	  else
	  {
		  ev = channel.getEventAfter(scrollPos);
	  }
	  if (ev != null)
      {
		    currentScrollPos = Math.min(ev.getStart(), upGrid.getEventsWidth());
			final float factor = ((float) (currentScrollPos - scrollPos) / surfaceSize);
			trans = f.createScrollingTransformation(400, factor, 0f);
		}
	  return trans;
	}

	private void setSelectedChannel(int newSelectedChannel) {
		if (this.selectedChannel != newSelectedChannel)
		{
			if (channels[selectedChannel].isSelected())
			{
				channels[selectedChannel].setSelected(false);
			}
			this.selectedChannel = newSelectedChannel;
		}
	}
	

	private int getSelectedChannel() {
		return selectedChannel ;
	}

	private NavigatorAPI readNavigatorAPI() {
		NavigatorAPI navigatorAPI = null;
		System.out.println(" - OsmApis.readNavigatorAPI()");
		try {
			navigatorAPI = (NavigatorAPI) IxcRegistry
					.lookup(context,
							"/2d/100/tv.osmosys.telenet.ixc.exporters.NavigatorAPIExporter");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		return navigatorAPI;
	}

}
