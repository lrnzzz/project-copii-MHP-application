package com.adb.gfxexample;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import com.adb.app.gfx.backend.Transformable;
import com.adb.app.gfx.backend.WindowPaintListener;
import com.adb.gfxexample.utils.Utils;

public class MenuItemRenderer implements WindowPaintListener {

	private Image icon;
	
	private Image icon2;
	
	private int x;
	
	private int y;
	
	private int selected = 0;

	private Transformable window;

	public MenuItemRenderer(Transformable window, String imgName, String imgName2)
	{
		this(window, imgName, imgName2, 0, 0);
	}
	
	public MenuItemRenderer(Transformable window, String imgName, String imgName2, int x, int y)
	{
		this.x = x;
		this.y = y;
		this.icon = Utils.loadImage(imgName);
		this.icon2 = Utils.loadImage(imgName2);
		this.window = window;
	}
	
	public Transformable getWindow() {
		return window;
	}
	
	public void setSelected(int selected) {
		this.selected = selected;
	}
	
	public int getSelected() {
		return this.selected;
	}
	
	public void paintWindow(Graphics g, Rectangle invalidBounds) {
		Image icon = selected == 0 ? this.icon : icon2;
        if (icon != null)
        {
    		g.setClip(invalidBounds.x, invalidBounds.y, invalidBounds.width, invalidBounds.height);
    		g.clearRect(invalidBounds.x, invalidBounds.y, invalidBounds.width, invalidBounds.height - 54);
            g.drawImage(icon, x, y, null);
        }
        
        
	}
	

}
