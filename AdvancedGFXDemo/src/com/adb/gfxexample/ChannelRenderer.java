package com.adb.gfxexample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import com.adb.app.gfx.backend.WindowPaintListener;

public class ChannelRenderer implements WindowPaintListener {

	final static Color FOCUSED_TEXT_COLOR = new Color(0xff, 0xb4, 0x00);
    final static Color NORMAL_TEXT_COLOR = new Color(0xFE, 0xFE, 0xFE);
	
	private Channel channel;

	public ChannelRenderer(Channel channel)
	{
		this.channel = channel;
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public void paintWindow(Graphics g, Rectangle invalidBounds, boolean paintSelected) {
		g.setClip(invalidBounds);
		g.clearRect(invalidBounds.x, invalidBounds.y, invalidBounds.width, invalidBounds.height);
				
		g.setColor(channel.isSelected() && paintSelected ? FOCUSED_TEXT_COLOR : RowRenderer.DARK_COLOR);
		g.drawLine(invalidBounds.x, invalidBounds.y + 0, invalidBounds.width, invalidBounds.y + 0);
		g.setColor(channel.isSelected() && paintSelected ? FOCUSED_TEXT_COLOR : RowRenderer.LIGHT_COLOR);
		g.drawLine(invalidBounds.x, invalidBounds.y + 1, invalidBounds.width, invalidBounds.y + 1);
		
		
		Image logo = channel.getLogo(paintSelected);
		if (logo != null)
		{
			g.drawImage(logo, invalidBounds.width - logo.getWidth(null) - 2, invalidBounds.y + (invalidBounds.height - logo.getHeight(null)) / 2, null);
		}
        String name = channel.getName();
		if (name != null)
        {
			g.setFont(EventsRenderer.DEFAULT_FONT);
			g.setColor(channel.isSelected() && paintSelected ? FOCUSED_TEXT_COLOR : NORMAL_TEXT_COLOR);
            g.setClip(invalidBounds.x, invalidBounds.y + 2, invalidBounds.width - 90, invalidBounds.height - 12);
            g.drawString(name, 40, invalidBounds.y + 35);
            String number = Integer.toString(channel.getNumber());
            int w = g.getFontMetrics().stringWidth(number);
            g.drawString(number, 30 - w, invalidBounds.y + 35);
        }
	}

	public void paintWindow(Graphics g, Rectangle invalidBounds) {
		paintWindow(g, invalidBounds, false);		
	}

}
