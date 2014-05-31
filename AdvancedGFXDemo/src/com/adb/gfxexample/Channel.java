package com.adb.gfxexample;

import java.awt.Image;

public class Channel {

	String name;
	
	int number;
	
	Image activeLogo;
	
	Image inactiveLogo;
	
	Event[] events;

	private boolean selected;

	private EventsRenderer eventsRenderer;

	private ChannelRenderer channelRenderer;

	private RowRenderer rowRenderer;

	private int currentScrollPos;
	
	public Channel(String name, int number, Image activeLogo, Image inactiveLogo, Event[] events)
	{
		this.name = name;
		this.number = number;
		this.activeLogo = activeLogo;
		this.inactiveLogo = inactiveLogo;
		this.events = events;
	}
	
	public String getName() {
		return name;
	}
	
	public int getNumber() {
		return number;
	}
	
	public Image getLogo() {
		return getLogo(false);
	}
	
	public Image getLogo(boolean selected) {
		return isSelected() && selected ? activeLogo : inactiveLogo;
	}
	
	public Event[] getEvents() {
		return events;
	}
	
	public boolean isSelected()
	{
		return selected;
	}
	
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
	
	public EventsRenderer getEventsRenderer()
	{
		if (eventsRenderer == null)
		{
			eventsRenderer = new EventsRenderer(getChannelRenderer());
		}
		return eventsRenderer;
	}

	public ChannelRenderer getChannelRenderer() {
		if(channelRenderer == null)
		{
			channelRenderer = new ChannelRenderer(this);
		}
		return channelRenderer;
	}
	
	public RowRenderer getRowRenderer()
	{
		if (rowRenderer == null)
		{
			rowRenderer = new RowRenderer(getChannelRenderer());
		}
		return rowRenderer;
	}

	public Event getEventAfter(int scrollPos) {
		Event result = null;
		if (events != null)
		{
			for (int i = 0; i < events.length; i++)
			{
				if (events[i].getStart() > scrollPos)
				{
					result = events[i];
					break;
				}
			}
		}
		return result;
	}
	
	public Event getEventBefore(int scrollPos) {
		Event result = null;
		if (events != null)
		{
			for (int i = 1; i < events.length; i++)
			{
				if (events[i].getStart() >= scrollPos)
				{
					result = events[i - 1];
					break;
				}
			}
		}
		return result;
	}
	
	public Event getEventAt(int scrollPos) {
		Event result = null;
		if (events != null)
		{
			for (int i = 0; i < events.length; i++)
			{
				if (events[i].getStart() + events[i].getDuration() > scrollPos)
				{
					result = events[i];
					break;
				}
			}
		}
		return result;
	}

	public void setEventsPos(int currentScrollPos) {
		this.currentScrollPos = currentScrollPos;
	}
	
	public int getEventsPos() {
		return currentScrollPos;
	}
}
