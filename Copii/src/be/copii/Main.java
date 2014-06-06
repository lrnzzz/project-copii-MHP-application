package be.copii;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.tv.service.SIRequest;
import javax.tv.xlet.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.dvb.event.EventManager;
import org.dvb.event.UserEventListener;
import org.dvb.event.UserEventRepository;
import org.dvb.io.ixc.IxcRegistry;
import org.dvb.si.SIDatabase;
import org.dvb.ui.DVBColor;
import org.dvb.ui.FontFormatException;

import org.havi.ui.*;
import org.havi.ui.event.HRcEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.zappware.ixc.TelenetAPI;


public class Main implements Xlet, UserEventListener {
	
	//global settings vars
	String SERVER_URL = "http://192.168.0.6";
	String ACCOUNT_NAME = "VTM KZOOM";

	//store actual xlet-context
	private XletContext ctx;
	private HScene scene;

	//colors
	Color clr1 = new DVBColor(255, 95, 34, 200);
	Color clr2 = new DVBColor(232, 57, 31, 200);
	Color clr3 = new DVBColor(255, 47, 59, 200);
	Color clr4 = new DVBColor(255, 196, 33, 200);
	Color clr5 = new DVBColor(248, 34, 255, 200);
	Color clr6 = new DVBColor(250, 250, 250, 200);
	Color clr7 = new DVBColor(204, 204, 204, 200);
	Color clr8 = new DVBColor(51, 51, 51, 200);

	//text labels
	private HStaticText lblCopii;
	private HStaticText lblFbText;
	private HStaticText lblFbCount;
	
	//images
	private CImage imgLogo = new CImage("be/copii/resource/logo.png", 10, 10);
	

	public void initXlet(XletContext ctx) throws XletStateChangeException {
		System.out.println("init copii remote helper");

		this.ctx = ctx;

		//create template
		HSceneTemplate sceneTemplate = new HSceneTemplate();

		//size & position
		sceneTemplate.setPreference(HSceneTemplate.SCENE_SCREEN_DIMENSION, new HScreenDimension(1.0f, 1.0f), HSceneTemplate.REQUIRED);
		sceneTemplate.setPreference(HSceneTemplate.SCENE_SCREEN_LOCATION, new HScreenPoint(0.0f, 0.0f), HSceneTemplate.REQUIRED);

		//get Scene instance from factory
		scene = HSceneFactory.getInstance().getBestScene(sceneTemplate);
		

		//:: LABELS
		//create label objects
		lblCopii = new HStaticText("Copii xlet is running");
		lblFbText = new HStaticText("Feedback");
		lblFbCount = new HStaticText("8990");

		//label properties
		lblCopii.setLocation(0, 0);
		lblCopii.setSize(720, 20);
		lblCopii.setFont(new Font("Helvetica", 200, 14));
		lblCopii.setForeground(clr6);
		lblCopii.setBackground(clr8);
		lblCopii.setBackgroundMode(HVisible.BACKGROUND_FILL);
		
		lblFbText.setLocation(100, 500);
		lblFbText.setSize(520, 40);
		lblFbText.setFont(new Font("Helvetica", 100, 15));
		lblFbText.setForeground(clr6);
		lblFbText.setBackground(clr2);
		lblFbText.setBackgroundMode(HVisible.BACKGROUND_FILL);

		
		lblFbCount.setLocation(620, 480);
		lblFbCount.setSize(80, 60);
		lblFbCount.setFont(new Font("Helvetica", 100, 25));
		lblFbCount.setForeground(clr2);
		lblFbCount.setBackground(clr6);
		lblFbCount.setBackgroundMode(HVisible.BACKGROUND_FILL);
		lblFbCount.setVisible(false);

		//lblFbCount.setTextContent("Lorenz", HState.NORMAL_STATE);
		//add labels to scene
		
		//scene.add(lblCopii);
		scene.add(lblFbText);
		scene.add(lblFbCount);
		
		scene.add(imgLogo);
	}

	
	public void startXlet() throws XletStateChangeException {
		System.out.println("start copii remote helper");
		
		//::IR
		//request EventManager
		EventManager eventManager = EventManager.getInstance();
		// Repository
		UserEventRepository eventRepository = new UserEventRepository("Voorbeeld");
		// Events toevoegen
		eventRepository.addAllArrowKeys();
		eventRepository.addAllNumericKeys();
		eventRepository.addKey(27); //exit
		eventRepository.addKey(org.havi.ui.event.HRcEvent.VK_COLORED_KEY_0);
		// Bekend maken bij EventManager
		eventManager.addUserEventListener(this, eventRepository);

		//:: show scene
		scene.validate();
		scene.setVisible(true);
		
		//:: get system inforamtion
		try {
			System.out.println("LANG: "+this.getTelenetAPI().getOnScreenLanguage());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


	public void destroyXlet(boolean unconditional) throws XletStateChangeException {
		if(unconditional) {
			//Destroy exlet
			System.out.println("The COPII Xlet will be terminated");
			if (scene != null) {
				scene.setVisible(false);
				HSceneFactory.getInstance().dispose(scene);
				scene.dispose();
				scene = null;
			}
		}
		else {
			throw new XletStateChangeException("Undefined exception");
		}
	}


	public void pauseXlet() {
		System.out.println("pause copii remote helper");
	}
	
	public String sendPostRequest(String urlstring, String requeststring) 
	{
		HttpConnection hc = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;

		String message = "";

		// specifying the query string
		try 
		{
			// open http connection with the web server
			// for both read and write access
			hc = (HttpConnection) Connector.open(urlstring, Connector.READ_WRITE);
			
			
			// setting the request method to POST
			hc.setRequestMethod(HttpConnection.POST);
			
			
			
			hc.setRequestProperty("User-Agent", "Copii MHP Helper Xlet");
			hc.setRequestProperty("Accept-Language", "nl-BE");
			hc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			// obtaining output stream for sending query string
			dos = hc.openDataOutputStream();
			byte[] request_body = requeststring.getBytes();

			// sending query string to web server
			for (int i = 0; i < request_body.length; i++)
			{
				dos.writeByte(request_body[i]);
			}

			// obtaining input stream for receiving HTTP response
			dis = new DataInputStream(hc.openInputStream());

			// reading the response from web server character by character
			int ch;
			while ((ch = dis.read()) != -1) 
			{
				message = message + (char) ch;
			}
		}
		catch (IOException ioe) 
		{
			message = "ERROR";
		} 
		finally 
		{
			// freeing up i/o streams and http connection
			try {
				if (dos != null) dos.close();
				if (dis != null) dis.close();
				if (hc != null) hc.close();	
			}
			catch(IOException ioe) {}
		}
		System.out.println(message);
		
		//::Parse json
		
		JSONObject json;
		JSONObject jsonParts;
		
		try {
			//parse message
			json = (JSONObject)new JSONParser().parse(message);
			String type = json.get("type").toString();
			String msg = json.get("message").toString();
			
			//show feedback on the screen
			lblFbText.setTextContent(msg, HState.NORMAL_STATE);
			
			//process data and do the right actions dependant from the command
			if((type).equals("success")) {
				String actionType = json.get("action_type").toString();
				
				//parse the parts object
				String parts = json.get("parts").toString();
				jsonParts = (JSONObject)new JSONParser().parse(parts);
				
				//Push
				if((actionType).equals("push")) {
					String message1 = jsonParts.get("message_1").toString();
					String message2 = jsonParts.get("message_2").toString();
					String action = jsonParts.get("action").toString();
					String times = jsonParts.get("times").toString();
					
					//show feedback on the screen
					lblFbCount.setVisible(true);
					lblFbCount.setTextContent(times + " X", HState.NORMAL_STATE);
				}
				
				//Audio
				if((actionType).equals("audio")) {
					String dB = jsonParts.get("dB").toString();
					
					//show feedback on the screen
					lblFbCount.setVisible(true);
					lblFbCount.setTextContent(dB + "\n dB", HState.NORMAL_STATE);
				}	
			}
			else {
				System.out.println("Opgelet: " + msg);
			}
			

		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}		

		return message;
	}
	
	public void doApiCall(String ds_hash, String ir_code, String value, String ip) {
		//ds_hash=IOYTS6820gdoy0hgdq7_SDAOh9&ir_code=1SGE66&value=1&ip=192.168.0.102"
		sendPostRequest(SERVER_URL + "/nl/v1/action", "ds_hash=" + ds_hash + "&ir_code=" + ir_code + "&value=" + value + "&ip=" + ip);
	}
	
	// Opvangen van de Key Events
	public void userEventReceived(org.dvb.event.UserEvent e) {
		String ip = "192.168.0.102";
		String ds_hash = "IOYTS6820gdoy0hgdq7_SDAOh9";

		if (e.getType() == KeyEvent.KEY_PRESSED) {
			switch (e.getCode()) {
			//Button 1
			case HRcEvent.VK_1:
				doApiCall(ds_hash, "1SGE66", "", ip);
				break;
				//Button 2
			case HRcEvent.VK_2:
				doApiCall(ds_hash, "2ID778", "", ip);
				break;
				//Button 3
			case HRcEvent.VK_3:
				doApiCall(ds_hash, "389SQN", "", ip);
				break;

				//Spinner Left
			case HRcEvent.VK_4:
				doApiCall(ds_hash, "457DSI", "-1", ip);
				break;
				//Spinner Right
			case HRcEvent.VK_5:
				doApiCall(ds_hash, "457DSI", "1", ip);
				break;

				//Shaker
			case HRcEvent.VK_6:
				//doApiCall(ds_hash, "554POU", "", ip);
				break;

				//Micro Niveau 1
			case HRcEvent.VK_7:
				doApiCall(ds_hash, "6ZDZFD", "1", ip);
				break;
				//Micro Niveau 2
			case HRcEvent.VK_8:
				doApiCall(ds_hash, "6ZDZFD", "2", ip);
				break;
				//Micro Niveau 3
			case HRcEvent.VK_9:
				doApiCall(ds_hash, "6ZDZFD", "3", ip);
				break;
				//Micro Niveau 4
			case HRcEvent.VK_0:
				doApiCall(ds_hash, "6ZDZFD", "4", ip);
				break;
				
				//RED KEY
			case HRcEvent.VK_COLORED_KEY_0:
				System.out.println("PRESSED RODE KNOP");
				break;

				//EXIT
			case 27:
				try {
					destroyXlet(true);
				} catch (XletStateChangeException e1) {
					e1.printStackTrace();
				}
				break;
			}
		}
	}
	
    public TelenetAPI getTelenetAPI() throws NotBoundException, RemoteException {
        TelenetAPI api = (TelenetAPI) IxcRegistry.lookup(this.ctx, "/1/1/telenet");
        return api;
    }
    
}