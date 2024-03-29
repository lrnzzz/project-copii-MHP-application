package be.copii.ajaxCall;

import org.havi.ui.*;
import java.awt.*;

public class CImage extends HComponent {
	private static final long serialVersionUID = 1983110799092674608L;
	
	public static int x = 0, y = 0;
	private java.awt.Image img;
	private MediaTracker mt;
	
	public CImage(String bitmap, int x, int y){
        img = this.getToolkit().getImage("ground.png");
        mt = new MediaTracker(this);
        mt.addImage(img, 0);
        try{mt.waitForAll();}
        catch(Exception e){System.out.println(e.toString());}
        this.setBounds(x, y, img.getWidth(null), img.getHeight(null));
    }
    
    public void paint(Graphics g){
        g.drawImage(img, x, y, null);
    }
}
