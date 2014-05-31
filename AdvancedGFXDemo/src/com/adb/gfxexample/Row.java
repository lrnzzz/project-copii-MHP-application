package com.adb.gfxexample;

import com.adb.app.gfx.backend.Transformable;

public class Row {
	Transformable channelWindow;
	Transformable rowWindow;
	Transformable eventsWindow;
	
	public Row(Transformable channelWindow, Transformable rowWindow, Transformable eventsWindow)
	{
		this.channelWindow = channelWindow;
		this.rowWindow = rowWindow;
		this.eventsWindow = eventsWindow;
	}
	
	public Transformable getChannelWindow() {
		return channelWindow;
	}
	
	public Transformable getRowWindow() {
		return rowWindow;
	}
	
	public Transformable getEventsWindow() {
		return eventsWindow;
	}
}
