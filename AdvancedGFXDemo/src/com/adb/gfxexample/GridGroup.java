package com.adb.gfxexample;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.adb.app.gfx.backend.Transformable;
import com.adb.app.gfx.backend.WindowManager;
import com.adb.app.gfx.backend.WindowPaintListener;
import com.adb.app.gfx.backend.transformation.Transformation;

public class GridGroup {

	private Channel[] channels;

	private WindowManager manager;

	private Transformable gridWindow;
	
	Transformable channelsWindow;
	Transformable eventsWindow;
	
	Transformable channelsWindowD;
	Transformable eventsWindowD;
	
	Transformable timeLineWindow;
	

	public GridGroup(Channel[] channels, WindowManager manager, Transformable gridWindow) {
		this.channels = channels;
		this.manager = manager;
		this.gridWindow = gridWindow;
		
		int surfaceHeight = (54 * (channels.length + 3));
		int height = 54 * 4;
		
		this.channelsWindow = (Transformable)manager.requestWindow(gridWindow, new Rectangle(0, 50, 260, height), 0);
		Rectangle rec = new Rectangle(channelsWindow.getBounds());
		rec.height = surfaceHeight;
		channelsWindow.getSurface().setBounds(rec);
		this.channelsWindow.setWindowPaintListener(new InternalChannelRenderer());
		
		this.eventsWindow = (Transformable)manager.requestWindow(gridWindow, new Rectangle(260, 50, 1280 - 260 - 129, height), 0);
		rec = new Rectangle(eventsWindow.getBounds());
		rec.width += rec.width;
		rec.height = surfaceHeight;
		eventsWindow.getSurface().setBounds(rec);
		this.eventsWindow.setWindowPaintListener(new InternalEventsRenderer());
		
		this.channelsWindow.show();
		this.eventsWindow.show();
		
		
		height = 54 * 6;
		
		this.channelsWindowD = (Transformable)manager.requestWindow(gridWindow, new Rectangle(0, 4*54 + 50, 260, height), 0);
		rec = new Rectangle(channelsWindowD.getBounds());
		rec.height = surfaceHeight;
		channelsWindowD.getSurface().setBounds(rec);
		this.channelsWindowD.setWindowPaintListener(new InternalChannelRenderer());
		float factor = 1 * getRowFactor();
		this.channelsWindowD.setScrollingPosition(0f, factor);
		
		this.eventsWindowD = (Transformable)manager.requestWindow(gridWindow, new Rectangle(260, 4*54 + 50, 1280 - 260 - 129, height), 0);
		rec = new Rectangle(eventsWindowD.getBounds());
		rec.width += rec.width;
		rec.height = surfaceHeight;
		eventsWindowD.getSurface().setBounds(rec);
		this.eventsWindowD.setWindowPaintListener(new InternalEventsRenderer());
		this.eventsWindowD.setScrollingPosition(0f, factor);		
		
		this.channelsWindowD.show();
		this.eventsWindowD.show();
		
		this.timeLineWindow = (Transformable)manager.requestWindow(gridWindow, new Rectangle(260, 0, 1280 - 260 - 129, 54), 0);
		rec = new Rectangle(timeLineWindow.getBounds());
		rec.width += rec.width;
		this.timeLineWindow.getSurface().setBounds(rec);
		this.timeLineWindow.setWindowPaintListener(new InternalTimeLineRenderer());
		this.timeLineWindow.show();
	}

	public class InternalChannelRenderer implements WindowPaintListener {

		public void paintWindow(Graphics g, Rectangle invalidBounds) {
			Rectangle b = new Rectangle(0,0,invalidBounds.width, 54); 
			for (int i = 0; i < channels.length + 4; i++)
			{
				Channel channel = (Channel) channels[i % channels.length];
				channel.getChannelRenderer().paintWindow(g, b);
				b.y += 54;
			}
		}

	}

	public class InternalEventsRenderer implements WindowPaintListener {

		public void paintWindow(Graphics g, Rectangle invalidBounds) {
			Rectangle b = new Rectangle(invalidBounds); 
			b.height = 54;
			b.width = 2 * invalidBounds.width;
			for (int i = 0; i < channels.length + 4; i++)
			{
				Channel channel = (Channel) channels[i % channels.length];
				channel.getEventsRenderer().paintWindow(g, b);
				b.y += 54;
			}
		}

	}
	
	public class InternalTimeLineRenderer implements WindowPaintListener {

		public void paintWindow(Graphics g, Rectangle invalidBounds) {
			int x = 0;
			g.setFont(EventsRenderer.DEFAULT_FONT);
			int i = 0;
			while (x < 2 * invalidBounds.width)
			{
				g.setColor(EventsRenderer.SELECTED_TEXT_COLOR);
				g.drawString("0" + (i >> 1) + ":" + ((i % 2) == 1 ? "3" : "0") + "0", x, invalidBounds.y + 35);
				g.setColor(EventsRenderer.NORMAL_TEXT_COLOR);
				g.drawString("*", x + 120 , invalidBounds.y + 40);
				x += 200;
				i++;
			}
		}

	}

	public void applyTransformation(Transformation trans) {
		channelsWindow.applyTransformation(trans);
		eventsWindow.applyTransformation(trans);
		channelsWindowD.applyTransformation(trans);
		eventsWindowD.applyTransformation(trans);
	}

	public void applyEventsTransformation(Transformation trans) {
		eventsWindow.applyTransformation(trans);
		eventsWindowD.applyTransformation(trans);
		timeLineWindow.applyTransformation(trans);
	}

	public int getEventsSurfaceWidth() {
		return eventsWindow.getSurface().getBounds().width;
	}

	public int getEventsWidth() {
		return eventsWindow.getBounds().width;
	}

	public float getRowFactor() {
		return (float) 54 / channelsWindow.getSurface().getBounds().height;
	}
}
