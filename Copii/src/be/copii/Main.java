package be.copii;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.tv.xlet.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.dvb.event.EventManager;
import org.dvb.event.UserEventListener;
import org.dvb.event.UserEventRepository;

import org.havi.ui.*;
import org.havi.ui.event.HRcEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main implements Xlet, UserEventListener {

	//store actual xlet-context
	private XletContext actualXletContext;
	private HScene scene;

	//colors
	Color clr1 = new Color(0xff, 0x5f, 0x22);
	Color clr2 = new Color(0xe8, 0x39, 0x1f);
	Color clr3 = new Color(0xff, 0x2f, 0x3b);
	Color clr4 = new Color(0xff, 0xc4, 0x21);
	Color clr5 = new Color(0xf8, 0x22, 0xff);
	Color clr6 = new Color(0xfa, 0xfa, 0xfa);
	Color clr7 = new Color(0xb2, 0xb2, 0xb2);
	Color clr8 = new Color(0x33, 0x33, 0x33);

	//text labels
	private HStaticText lblCopii;


	public void initXlet(XletContext ctx) throws XletStateChangeException {
		System.out.println("init copii remote helper");

		this.actualXletContext = ctx;

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

		//label properties
		lblCopii.setLocation(0,0);
		lblCopii.setSize(720, 40);
		lblCopii.setForeground(clr6);
		lblCopii.setBackground(clr3);
		lblCopii.setBackgroundMode(HVisible.BACKGROUND_FILL);

		//lblCopii.setTextContent("Lorenz", HState.NORMAL_STATE);
		//add labels to scene
		scene.add(lblCopii);
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
		// Bekend maken bij EventManager
		eventManager.addUserEventListener(this, eventRepository);



		
		
		//:: show scene
		scene.validate();
		scene.setVisible(true);
	}


	public void destroyXlet(boolean unconditional) throws XletStateChangeException {
		if(unconditional) {
			System.out.println("The Xlet must be terminated");
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
		//System.out.println(message);
		
		//parse json
		JSONObject json;
		
		try {
			json = (JSONObject)new JSONParser().parse(message);
			
			String status = json.get("status").toString();
			String type = json.get("type").toString();
			String msg = json.get("message").toString();
			
			
			System.out.println("status: " + status);
			System.out.println("type: " + type);
			System.out.println("message: " + msg);
	

			
			
			
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	public void doApiCall(String ds_hash, String ir_code, String value, String ip) {
		//ds_hash=IOYTS6820gdoy0hgdq7_SDAOh9&ir_code=1SGE66&value=1&ip=192.168.0.102"
		sendPostRequest("http://192.168.0.5/nl/v1/action", "ds_hash=" + ds_hash + "&ir_code=" + ir_code + "&value=" + value + "&ip=" + ip);
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
					//doApiCall(ds_hash, "457DSI", "left", ip);
					break;
				//Spinner Right
				case HRcEvent.VK_5:
					//doApiCall(ds_hash, "457DSI", "right", ip);
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
			}
		}
	}
}
