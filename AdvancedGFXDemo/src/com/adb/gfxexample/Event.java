package com.adb.gfxexample;

import java.awt.Image;

public class Event {

	String name;
	
	int duration;
	
	int start;
	
	String category;
	
	String desc;

	private Image image;
	
	public Event(String name, String category, String desc, Image image, int startTime, int duration)
	{
		this.name = name;
		this.start = startTime;
		this.duration = duration;
		this.category = category;
		this.desc = desc;
		this.image = image;
	}
	
	public String getName() {
		return name;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public int getStart() {
		return start;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public Image getImage() {
		return image;
	}
	
	public Event getClone()
	{
		return new Event(name, category, desc, image, start, duration);
	}
}
