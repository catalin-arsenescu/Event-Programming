package com.eventprogramming.gui.logic;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;

public interface PageMediator {

	void registerControl(Control control, String identifier);
	
	void notifyEvent(Control control, SelectionEvent event);
	
}
