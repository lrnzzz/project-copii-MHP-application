package be.copii;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.tv.xlet.*;
import org.dvb.ui.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.havi.ui.*;

import be.copii.utils.*;


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

		String url = "http://api.openweathermap.org/data/2.5/weather?q=Antwerp,be";
		HttpConnection connection = null;
		InputStream inputStream = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			// open http connection
			connection = (HttpConnection) Connector.open(url);
			
			//Set request method
			connection.setRequestMethod(HttpConnection.GET); 			
			//Set header information (this header is optional)			
			connection.setRequestProperty("User-Agent", "Profile/MIDP-1.0 Configuration/CLDC-1.0"); 			


			// establishing input stream from the connection
			inputStream = connection.openInputStream();

			byte[] buffer = new byte[512];
			// reading the response from web server, character by character
			int red;
			while ((red = inputStream.read(buffer)) != -1 ) {
				outputStream.write(buffer, 0, red);
			}
		} 
		catch (IOException ioe) {
			System.out.println("HTTP NOT OK");
		} 
		
		finally {
			try { if(connection != null)  connection.close(); } catch (IOException ignored) {}
			try { if(inputStream != null) inputStream.close();} catch (IOException ignored) {} 
		}
		String output = new String(outputStream.toByteArray());
		
		System.out.println(output);
		

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
	
	
	



}
