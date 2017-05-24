package com.eventprogramming.gui.logic;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;

public class DefaultSelectionListener implements SelectionListener {

	private Control fControl;
	private PageMediator fMediator;

	public DefaultSelectionListener(PageMediator mediator, Control control) {
		fMediator = mediator;
		fControl = control;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		fMediator.notifyEvent(fControl, e);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}
}
