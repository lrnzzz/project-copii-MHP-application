package com.adb.gfxexample;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.adb.app.gfx.backend.WindowPaintListener;

public class EventsRenderer implements WindowPaintListener {
	   
	public final static Color FOCUSED_TEXT_COLOR = new Color(0xff, 0xb4, 0x00);
	public final static Color SELECTED_TEXT_COLOR = new Color(0xd7, 0xd6, 0xd1);
    public final static Color NORMAL_TEXT_COLOR = new Color(0x98, 0x98, 0x98);
    
	public static final String TIRESIAS = "Tiresias";
	private static String DEFAULT_FONT_NAME = TIRESIAS;
	public static final Font DEFAULT_FONT = new Font(DEFAULT_FONT_NAME, Font.PLAIN, 20);
	public static final Font CATEGORY_FONT = new Font(DEFAULT_FONT_NAME, Font.PLAIN, 16);
    
	private ChannelRenderer channelRenderer;

	public EventsRenderer(ChannelRenderer channelRenderer) {
		this.channelRenderer = channelRenderer;
	}
	
	public void paintWindow(Graphics g, Rectangle invalidBounds)
	{
		 paintWindow(g, invalidBounds, false, false);
	}

	public void paintWindow(Graphics g, Rectangle invalidBounds, boolean fromSelected, boolean paintSelected) {
		g.setClip(invalidBounds);
		Channel channel = channelRenderer.getChannel();
		Event[] events = channel.getEvents();
		int currentL = 0;
		int i = 0;
		
		Event currentEvent = channel.getEventAt(channel.getEventsPos());
		if(fromSelected)
		{
			for (int j = 0; j < events.length; j++)
			{
				if (events[j] == currentEvent)
				{
					i = j;
					break;
				}
			}
		}
		
		g.setColor(RowRenderer.DARK_COLOR);
		g.drawLine(invalidBounds.x, invalidBounds.y, invalidBounds.width, invalidBounds.y);

		g.setColor(RowRenderer.LIGHT_COLOR);
		g.drawLine(invalidBounds.x, invalidBounds.y + 1, invalidBounds.width, invalidBounds.y + 1);
		
		while(currentL < invalidBounds.width)
		{
			Event event = events[i];
			
			int dur = Math.min(event.getStart() - channel.getEventsPos() + event.getDuration(), event.getDuration());
			int pos = invalidBounds.x + currentL + dur - 2;
			
			g.setClip(invalidBounds);
			
			g.setColor((channel.isSelected() && paintSelected && event == currentEvent) ? FOCUSED_TEXT_COLOR : RowRenderer.DARK_COLOR);
			g.drawLine(pos, invalidBounds.y, pos, invalidBounds.height + invalidBounds.y);
			g.setColor((channel.isSelected()  && paintSelected && event == currentEvent) ? FOCUSED_TEXT_COLOR : RowRenderer.LIGHT_COLOR);
			g.drawLine(pos + 1, invalidBounds.y, pos + 1, invalidBounds.y + invalidBounds.height);
			
			if (channel.isSelected() && paintSelected)
			{
				g.setColor(FOCUSED_TEXT_COLOR);
				int posY = invalidBounds.y + (event == currentEvent ? 0 : invalidBounds.height - 2);
				g.drawLine(invalidBounds.x + currentL - 2, posY, pos, posY);
				g.drawLine(invalidBounds.x + currentL - 2, posY + 1, pos, posY +1);
			}
			
			g.setClip(invalidBounds.x + currentL, invalidBounds.y , dur - 12, invalidBounds.y + invalidBounds.height);
			g.setColor(channel.isSelected() && paintSelected ? ( event == currentEvent ? FOCUSED_TEXT_COLOR : SELECTED_TEXT_COLOR) : NORMAL_TEXT_COLOR);
			g.setFont(DEFAULT_FONT);
			g.drawString(event.getName(), invalidBounds.x + currentL + 10, invalidBounds.y + 35);
			
			currentL += dur;
		    i++;
		}
		
	}

}
