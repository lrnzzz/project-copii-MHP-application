package be.copii.ajaxCall;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;

public class Utils {

	private static final Component COMP = new Component()
    {
        private static final long serialVersionUID = 1L; /* */
    };
    
    final static MediaTracker tracker = new MediaTracker(COMP);
	
	static public Image loadImage(String imgLoc) {
        final Toolkit t = Toolkit.getDefaultToolkit();
        
        Image icon = t.getImage(imgLoc);
        tracker.addImage(icon, icon.hashCode());

		return icon;
	}
	
	public static final void waitForLoadAllImages()
	{
		try
		{
			tracker.waitForAll();
		}
		catch (final InterruptedException e)
		{
			System.out.println("loading interrupted");
		}
	}
}
