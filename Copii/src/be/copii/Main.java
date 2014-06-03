package be.copii;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.tv.xlet.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.havi.ui.*;

public class Main implements Xlet {

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


		//:: api call
		sendPostRequest("http://192.168.0.5/nl/v1/action", "ds_hash=IOYTS6820gdoy0hgdq7_SDAOh9&ir_code=1SGE66&value=1&ip=192.168.0.102");

		// show scene
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
		System.out.println(message);
		return message;
	}

}
