	package com.adb.gfxexample;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import com.adb.app.gfx.backend.WindowPaintListener;
import com.adb.gfxexample.utils.Utils;

public class ImageRenderer implements WindowPaintListener {

	private Image icon;
	
	private int x;
	
	private int y;

	public ImageRenderer(String imgName)
	{
		this(imgName, 0, 0);
	}
	
	public ImageRenderer(String imgName, int x, int y)
	{
		this.x = x;
		this.y = y;
		this.icon = Utils.loadImage(imgName);
	}
	
	public void paintWindow(Graphics g, Rectangle invalidBounds) {
		g.setClip(invalidBounds.x, invalidBounds.y, invalidBounds.width, invalidBounds.height);
		g.clearRect(invalidBounds.x, invalidBounds.y, invalidBounds.width, invalidBounds.height - 54);
        if (icon != null)
        {
            g.drawImage(icon, x, y, null);
        }
        
        
	}
	

}
