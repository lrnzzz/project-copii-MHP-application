package com.adb.gfxexample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import org.dvb.ui.DVBColor;

import com.adb.app.gfx.backend.WindowPaintListener;

public class RowRenderer implements WindowPaintListener {

	public final static Color LIGHT_COLOR = new DVBColor(255,255,255,29);
    public final static Color DARK_COLOR = new DVBColor(0,0,0,150);
    
	private ChannelRenderer channelRenderer;

	public RowRenderer(ChannelRenderer channelRenderer)
	{
		this.channelRenderer = channelRenderer;
	}
	
	public void paintWindow(Graphics g, Rectangle invalidBounds) {

		if (!channelRenderer.getChannel().isSelected())
		{
			g.setColor(DARK_COLOR);
			g.drawLine(invalidBounds.x, invalidBounds.y, invalidBounds.width, invalidBounds.y);
			//g.drawLine(invalidBounds.x, invalidBounds.height, invalidBounds.width, invalidBounds.height);

			g.setColor(LIGHT_COLOR);
			g.drawLine(invalidBounds.x, invalidBounds.y + 1, invalidBounds.width, invalidBounds.y + 1);
			//g.drawLine(invalidBounds.x, invalidBounds.height + 1, invalidBounds.width, invalidBounds.height + 1);
		}
	}

}
