package com.adb.gfxexample;

import java.awt.Rectangle;

import com.adb.app.gfx.backend.Transformable;
import com.adb.app.gfx.backend.transformation.Transformation;

public class SelectorGroup {
	
	Transformable windowUp;
	
	Transformable windowDown;

	private SelectorRenderer eventDetailRenderer;
	
	public SelectorGroup(Transformable windowUp, Transformable windowDown, SelectorRenderer eventDetailRenderer)
	{
		this.windowUp = windowUp;
		this.windowDown = windowDown;	
		this.eventDetailRenderer = eventDetailRenderer;
	}
	
	public void applyTransformation(Transformation trans)
	{
		if (windowUp != null)
		{
			windowUp.applyTransformation(trans);
		}
		if (windowDown != null)
		{
			windowDown.applyTransformation(trans);
		}
	}

	public void setSelectedChannel(Channel channel) {
		if (eventDetailRenderer != null)
		{
			eventDetailRenderer.setSelectedChannel(channel);
		}
	}

	public void show() {
		windowUp.show();
		windowDown.show();
	}
	
	public void hide() {
		windowUp.hide();
		windowDown.hide();
	}

	public void moveY(int i) {
        Rectangle b = windowUp.getBounds();
        b.y += i;
		windowUp.setBounds(b);

        b = windowDown.getBounds();
        b.y += i;
        windowDown.setBounds(b);
	}
}
