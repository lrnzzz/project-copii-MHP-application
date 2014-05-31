package be.copii;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.tv.xlet.*;
import org.dvb.ui.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

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
		
		//::COPII LABEL
		//create object
		lblCopii = new HStaticText("Copii xlet is running");
		
		//set properties
		lblCopii.setLocation(0,0);
		lblCopii.setSize(250, 40);
		lblCopii.setForeground(clr6);
		lblCopii.setBackground(clr3);
		lblCopii.setBackgroundMode(HVisible.BACKGROUND_FILL);
		
		//add label to scene
		scene.add(lblCopii);
		


	}
	
	public void startXlet() throws XletStateChangeException {
		System.out.println("start copii remote helper");
		
		
		//::TEST api call
//		String url = "http://api.openweathermap.org/data/2.5/weather?q=Antwerp,be";
//		
//		// new http connection
//		try {
//			HttpConnection con = (HttpConnection) Connector.open(url, Connector.READ_WRITE, true);
//			con.setRequestMethod(HttpConnection.GET);
//			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Series40)");
//			con.setRequestProperty("Connection", "close");          
//			int responseCode = con.getResponseCode();
//
//			if(responseCode == HttpConnection.HTTP_OK) {
//				// handle result, read data ...
//				System.out.println("HTTP 200 OK");
//			} else {
//				System.out.println("HTTP NOT OK");         
//			}
//			con.close();
//		}
//		catch(IOException ie) {
//			ie.printStackTrace();
//		}
		
		
		int iResponseCode;
		HttpConnection http = null;		
		InputStream iStrm = null;
		//String url = "http://api.openweathermap.org/data/2.5/weather?q=Antwerp,be";
		String url = "https://www.kernel.org/pub/software/scm/git/docs/v1.7.9.1/git-check-attr.txt";
		
		try { 
			try { 			
				http = (HttpConnection) Connector.open(url); 			
				// ----------------			
				// Client Request			
				// ----------------			
				// 1) Set request method			
				http.setRequestMethod(HttpConnection.GET); 			
				// 2) Set header information (this header is optional)			
				http.setRequestProperty("User-Agent",					
						"Profile/MIDP-1.0 Configuration/CLDC-1.0"); 			
				// 3) Set body/data - No data for this request 			
				// ----------------			
				// Server Response			
				// ----------------			
				// 1) Get response status message and code			
				iResponseCode = http.getResponseCode(); 	
				System.out.println("Msg: " + http.getResponseMessage());			
				System.out.println("Code: " + iResponseCode); 			
				// 2) Check response status			
				if (iResponseCode == HttpConnection.HTTP_OK) {	
					// 3) Get data and show the file contents	
					iStrm = http.openInputStream();				
					int length = (int) http.getLength();				
					if (length > 0) {					
						byte serverData[] = new byte[length];					
						iStrm.read(serverData);
						//get the data
					}			
				}
				else {
					System.out.println("No data can be read if response was not HTTP_OK");
				}
					
			}catch (Exception e){
				e.printStackTrace();
			}
			finally 
			{ 			
				// Close the stream
				if (iStrm != null)				
					iStrm.close();			
				// Close the connection
				if (http != null)				
					http.close();
			}
		}
		catch(IOException ie) {
			ie.printStackTrace();
		}
			
		
		
		
		
		
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