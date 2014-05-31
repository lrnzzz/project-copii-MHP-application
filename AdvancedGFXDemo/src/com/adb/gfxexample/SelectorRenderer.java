package com.adb.gfxexample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import com.adb.app.gfx.backend.Transformable;
import com.adb.app.gfx.backend.WindowPaintListener;
import com.adb.gfxexample.utils.Utils;

public class SelectorRenderer implements WindowPaintListener {

	private Image icon;

	private Transformable selector;

	private Channel selectedChannel;
	private String eventName;
	private String eventCategory;
	private String eventDesc;

	private Image eventImage;

	private Image progressBar;

	private Image iconUp;

	public SelectorRenderer(Transformable selector)
	{
		this.icon = Utils.loadImage("img/gid_bg_channel_focus_down.png");
		this.iconUp = Utils.loadImage("img/gid_bg_channel_focus_up.png");
		this.progressBar = Utils.loadImage("img/progressBar.png");;
		this.selector = selector;
	}
	
	public void paintWindow(Graphics g, Rectangle invalidBounds) {
		
		Channel selectedChannel = this.selectedChannel;
		String eventName = this.eventName;
		String eventCategory = this.eventCategory;
		String eventDesc = this.eventDesc;

		Image eventImage = this.eventImage;
		
		g.setClip(invalidBounds.x, invalidBounds.y, invalidBounds.width, invalidBounds.height);
		g.clearRect(invalidBounds.x, invalidBounds.y, invalidBounds.width, invalidBounds.height);
        if (icon != null)
        {
            g.drawImage(icon, 0, 54, null);
        }
        
        if (selectedChannel != null)
        {
        	g.setColor(EventsRenderer.FOCUSED_TEXT_COLOR);
        	g.drawLine(invalidBounds.x, invalidBounds.y + 197, invalidBounds.width, 197);
        	g.drawLine(invalidBounds.x, invalidBounds.y + 198, invalidBounds.width, 198);
            if (iconUp != null)
            {
                g.drawImage(iconUp, 0, 0, null);
            }
            Rectangle b = new Rectangle(0,0,260, 54);
        	selectedChannel.getChannelRenderer().paintWindow(g, b, true);
            b.x += 260;
            b.width = invalidBounds.width - 260;
        	selectedChannel.getEventsRenderer().paintWindow(g, b, true, true);
        }
        
        if (eventName != null)
        {
        	g.setClip(invalidBounds.x, invalidBounds.y, invalidBounds.width, invalidBounds.height);
        	g.drawImage(progressBar, invalidBounds.width - 320, 74, null);
	        if (eventImage != null)
	        {
	        	g.drawImage(eventImage, 254 - eventImage.getWidth(null), 54, null);
	        	g.setColor(Color.GRAY);
	        	g.drawRect(254 - eventImage.getWidth(null), 54, eventImage.getWidth(null), eventImage.getHeight(null));
	        }
			g.setClip(invalidBounds.x + 266, 0, 500, 200);
        	g.setFont(EventsRenderer.DEFAULT_FONT);
        	g.setColor(EventsRenderer.SELECTED_TEXT_COLOR);
			g.drawString(eventName, invalidBounds.x + 266, invalidBounds.y + 90);
			
			g.setFont(EventsRenderer.CATEGORY_FONT);
	        if (eventCategory != null)
	        {
				g.drawString(eventCategory, invalidBounds.x + 266, invalidBounds.y + 120);
	        }
	        if (eventDesc != null)
	        {
				g.drawString(eventDesc, invalidBounds.x + 266, invalidBounds.y + 140);
	        }
        }
	}
	
	public Transformable getSelector() {
		return selector;
	}
	
	void setSelectedChannel(Channel ch)
	{
		if (selectedChannel != ch)
		{
			selectedChannel = ch;
			if (selectedChannel != null)
			{
				Event event = selectedChannel.getEventAt(selectedChannel.getEventsPos());
				eventName = event.getName();
				eventCategory = event.getCategory();
				eventDesc = event.getDesc();
				eventImage = event.getImage();
			}
			selector.invalidate(null);
		}
		
	}

}
