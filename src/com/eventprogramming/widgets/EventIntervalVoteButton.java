package com.eventprogramming.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.eventprogramming.event.EventInterval;

public class EventIntervalVoteButton extends Button {

	public enum EventType {
		YES, NO, IFB
	}
	
	private EventInterval fInterval;
	private EventType fType;
	private List<EventIntervalVoteButton> others;

	public EventIntervalVoteButton(Composite parent, EventInterval interval, EventType type) {
		super(parent, SWT.CHECK);
		fInterval = interval;
		fType = type;
		addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				for (EventIntervalVoteButton b : others)
					b.setSelection(false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			
		});
	}

	public EventType getType() {
		return fType;
	}
	
	public void setOthers(EventIntervalVoteButton other1, EventIntervalVoteButton other2) {
		others = new ArrayList<>();
		others.add(other1);
		others.add(other2);
	}
	
	@Override
	protected void checkSubclass() {}
}
