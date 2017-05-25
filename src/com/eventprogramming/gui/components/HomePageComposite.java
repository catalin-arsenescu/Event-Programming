package com.eventprogramming.gui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.eventprogramming.gui.logic.DefaultSelectionListener;
import com.eventprogramming.gui.logic.PageMediator;

public class HomePageComposite extends Composite {

	private PageMediator fMediator;
	
	public HomePageComposite(Composite parent, int style, PageMediator mediator) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		fMediator = mediator;
		buildComposite();
	}

	private void buildComposite() {

		Group mainGroup = new Group(this, SWT.NONE);
		mainGroup.setLayout(new GridLayout(2, true));
		mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

		Button createEventButton = new Button(mainGroup, SWT.NONE);
		createEventButton.setText("Create an Event");
		createEventButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createEventButton.addSelectionListener(new DefaultSelectionListener(fMediator, createEventButton));
		fMediator.registerControl(createEventButton, "createEventButton");
		
		Button joinEventButton = new Button(mainGroup, SWT.NONE);
		joinEventButton.setText("Join an Event");
		joinEventButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		joinEventButton.addSelectionListener(new DefaultSelectionListener(fMediator, joinEventButton));
		fMediator.registerControl(joinEventButton, "joinEventButton");
		
		Button eventAdminButton = new Button(mainGroup, SWT.NONE);
		eventAdminButton.setText("Administrate your events");
		eventAdminButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		eventAdminButton.addSelectionListener(new DefaultSelectionListener(fMediator, eventAdminButton));
		fMediator.registerControl(eventAdminButton, "eventAdminButton");
	}
}
