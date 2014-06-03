package be.copii;

import org.havi.ui.*;
import java.awt.*;


public class CImage extends HComponent{
	private static final long serialVersionUID = -100653067764193601L;
	private Image bmap;
	private MediaTracker mtrack;
	//Plaats en locatie instellen in de constructor
	public CImage(String bitmapnaam, int x , int y) {
		bmap = this.getToolkit().getImage(bitmapnaam);
		mtrack = new MediaTracker(this);
		mtrack.addImage(bmap, 0) ;
		try
		{
			mtrack.waitForAll(); // wacht tot alle bitmaps geladen zijn
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		this.setBounds(x ,y ,bmap.getWidth(null), bmap.getWidth(null));
		// opgegeven plaats en afmeting van de bitmap
	}
	// Paint methode overriden
	public void paint (Graphics g) {
		g.drawImage(bmap, 0, 0, null);
	}
}
