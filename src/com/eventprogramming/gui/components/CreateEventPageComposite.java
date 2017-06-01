package com.eventprogramming.gui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import com.eventprogramming.gui.logic.DefaultSelectionListener;
import com.eventprogramming.gui.logic.PageMediator;

public class CreateEventPageComposite extends PageComposite {

	public CreateEventPageComposite(Composite parent, int style, PageMediator mediator) {
		super(parent, style, mediator);
		buildComposite();
	}

	protected void buildComposite() {

		Group mainGroup = new Group(this, SWT.NONE);
		mainGroup.setLayout(new GridLayout(2, true));
		mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		
		Label eventNameLabel = new Label(mainGroup, SWT.NONE);
		eventNameLabel.setText("Event name:");
		eventNameLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Text eventNameText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		eventNameText.setText("Event Programming Opening Ceremony");
		eventNameText.setTextLimit(100);
		eventNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fMediator.registerControl(eventNameText, "eventNameText");
		
		Label typeLabel = new Label(mainGroup, SWT.NONE);
		typeLabel.setText("Event type:");
		typeLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));

		Group eventTypeButtonsGroup = new Group(mainGroup, SWT.NONE);
		eventTypeButtonsGroup.setLayout(new GridLayout(2, true));
		eventTypeButtonsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		
		Button greedyTypeButton = new Button(eventTypeButtonsGroup, SWT.RADIO);
		greedyTypeButton.setText("Greedy");
		greedyTypeButton.setSelection(true);
		greedyTypeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fMediator.registerControl(greedyTypeButton, "greedyTypeButton");
		
		Button freeTypeButton = new Button(eventTypeButtonsGroup, SWT.RADIO);
		freeTypeButton.setText("Free");
		freeTypeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fMediator.registerControl(freeTypeButton, "freeTypeButton");
		
		Label startDateLabel = new Label(mainGroup, SWT.NONE);
		startDateLabel.setText("Earliest starting date:");
		startDateLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		DateTime startDateTime = new DateTime(mainGroup, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
		startDateTime.setDate(2017, 7, 1);
		startDateTime.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fMediator.registerControl(startDateTime, "startDateTime");
		
		Label endDateLabel = new Label(mainGroup, SWT.NONE);
		endDateLabel.setText("Latest ending date:");
		endDateLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		DateTime endDateTime = new DateTime(mainGroup, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
		endDateTime.setDate(2017, 7, 13);
		endDateTime.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fMediator.registerControl(endDateTime, "endDateTime");
		
		Label durationLabel = new Label(mainGroup, SWT.NONE);
		durationLabel.setText("Duration:");
		durationLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Spinner durationSpinner = new Spinner(mainGroup, SWT.NONE);
		durationSpinner.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		durationSpinner.setMinimum(0);
		durationSpinner.setMaximum(23);
		durationSpinner.setSelection(4);
		durationSpinner.setIncrement(1);
		fMediator.registerControl(durationSpinner, "durationSpinner");
		
		Label startHourLabel = new Label(mainGroup, SWT.NONE);
		startHourLabel.setText("Earliest start hour:");
		startHourLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Spinner startHourSpinner = new Spinner(mainGroup, SWT.NONE);
		startHourSpinner.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		startHourSpinner.setMinimum(0);
		startHourSpinner.setMaximum(23);
		startHourSpinner.setSelection(16);
		startHourSpinner.setIncrement(1);
		fMediator.registerControl(startHourSpinner, "startHourSpinner");
		
		Label endHourLabel = new Label(mainGroup, SWT.NONE);
		endHourLabel.setText("Latest ending hour:");
		endHourLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Spinner endHourSpinner = new Spinner(mainGroup, SWT.NONE);
		endHourSpinner.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		endHourSpinner.setMinimum(0);
		endHourSpinner.setMaximum(24);
		endHourSpinner.setSelection(24);
		endHourSpinner.setIncrement(1);
		fMediator.registerControl(endHourSpinner, "endHourSpinner");
		
		Button buttonCreateEvent = new Button(this, SWT.PUSH);
		buttonCreateEvent.setText("Create Event");
		buttonCreateEvent.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		fMediator.registerControl(buttonCreateEvent, "buttonCreateEvent");
		buttonCreateEvent.addSelectionListener(new DefaultSelectionListener(fMediator, buttonCreateEvent));
	}
}
