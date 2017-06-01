package com.eventprogramming.widgets;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.eventprogramming.client.ClientGUI;

public class BackButton extends Button {

	public BackButton(Composite parent, int style) {
		super(parent, style);
		
		addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ClientGUI.INSTANCE.goBack();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			
		});
	}

	@Override
	protected void checkSubclass() {}
}
