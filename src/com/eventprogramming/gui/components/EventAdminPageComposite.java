package com.eventprogramming.gui.components;

import java.util.Date;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.eventprogramming.event.Event;
import com.eventprogramming.event.EventInterval;
import com.eventprogramming.event.IntervalVote.VoteType;
import com.eventprogramming.gui.logic.DefaultSelectionListener;
import com.eventprogramming.gui.logic.PageMediator;
import com.eventprogramming.utils.Utils;

public class EventAdminPageComposite extends Composite {

	private static final String ACTIVITY_LOG = "Activity Log";
	private static final String VOTE_INFORMATION = "Vote Information";
	private static final String MANAGE_POLLS = "Manage Polls";
	private static final String EVENT_INFORMATION = "Event Information";
	private PageMediator fMediator;
	private String[] eventNames;
	
	public EventAdminPageComposite(Composite parent, int style, PageMediator mediator, String[] eventNames) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		fMediator = mediator;
		fMediator.registerControl(this, "page-composite");
		this.eventNames = eventNames;
		buildComposite();
	}

	private void buildComposite() {
		Group eventSelectGroup = new Group(this, SWT.NONE);
		eventSelectGroup.setLayout(new GridLayout(2, true));
		eventSelectGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Label eventSelectTitle = new Label(eventSelectGroup, SWT.NONE);
		eventSelectTitle.setText("Select event:");
		eventSelectTitle.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		
		Combo combo = new Combo(eventSelectGroup, SWT.NONE);
		combo.setItems(eventNames);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fMediator.registerControl(combo, "combo");
		combo.addSelectionListener(new DefaultSelectionListener(fMediator, combo));

		TabFolder tabFolder = new TabFolder (this, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setVisible(true);
		fMediator.registerControl(tabFolder, "tabFolder");
	}

	public void buildTabComposites(TabFolder tabFolder, Event event) {
		if (tabFolder.getItemCount() <= 0) {
			TabItem item = new TabItem(tabFolder, SWT.NONE);
			item.setText(EVENT_INFORMATION);
			item.setControl(createEventInformationTab(tabFolder, event));
			
			item = new TabItem(tabFolder, SWT.NONE);
			item.setText(MANAGE_POLLS);
			item.setControl(createManagePollsTab(tabFolder, event));
			
			item = new TabItem(tabFolder, SWT.NONE);
			item.setText(VOTE_INFORMATION);
			item.setControl(createVotesTab(tabFolder, event));
			
			item = new TabItem(tabFolder, SWT.NONE);
			item.setText(ACTIVITY_LOG);
			item.setControl(createActivityLogTab(tabFolder, event));
		} else {
			TabItem[] items = tabFolder.getItems();
			for (TabItem item : items) {
				if (item.getText().equals(EVENT_INFORMATION))
					item.setControl(createEventInformationTab(tabFolder, event));
				if (item.getText().equals(MANAGE_POLLS))
					item.setControl(createManagePollsTab(tabFolder, event));
				if (item.getText().equals(VOTE_INFORMATION))
					item.setControl(createVotesTab(tabFolder, event));
				if (item.getText().equals(ACTIVITY_LOG))
					item.setControl(createActivityLogTab(tabFolder, event));
			}
		}
	}
	
	private Control createVotesTab(TabFolder tabFolder, Event event) {
		Button button = new Button (tabFolder, SWT.PUSH);
		button.setText ("Vote information page");
		return button;
	}

	private Control createManagePollsTab(TabFolder tabFolder, Event event) {
		Composite main = new Composite(tabFolder, SWT.NONE);
		main.setLayout(new GridLayout(1, false));

		Group mainGroup = new Group(main, SWT.NONE);
		mainGroup.setLayout(new GridLayout(1, true));
		mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Table table = new Table (mainGroup, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
		data.heightHint = 200;
		table.setLayoutData(data);
		String[] titles = {"Date", "Starting hour", "Ending hour", "YES Votes", "NO Votes", "IFNEEDBE Votes"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText(titles[i]);
		}
		buildTableItems(table, event);
		for (int i=0; i<titles.length; i++) {
			table.getColumn(i).pack();
		}

		Group buttonsGroup = new Group(main, SWT.NONE);
		buttonsGroup.setLayout(new GridLayout(2, true));
		buttonsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Group addGroup = new Group(buttonsGroup, SWT.NONE);
		addGroup.setLayout(new GridLayout(2, true));
		addGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Label dateLabel = new Label(addGroup, SWT.NONE);
		dateLabel.setText("Earliest starting date:");
		dateLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		DateTime dateTime = new DateTime(addGroup, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
		dateTime.setDate(2017, 7, 1);
		dateTime.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fMediator.registerControl(dateTime, "dateTime");
		
		Label startHourLabel = new Label(addGroup, SWT.NONE);
		startHourLabel.setText("Earliest start hour:");
		startHourLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Spinner startHourSpinner = new Spinner(addGroup, SWT.NONE);
		startHourSpinner.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		startHourSpinner.setMinimum(event.getStartHour());
		startHourSpinner.setMaximum(event.getEndHour());
		startHourSpinner.setSelection(event.getStartHour());
		startHourSpinner.setIncrement(1);
		fMediator.registerControl(startHourSpinner, "startHourSpinner");
		
		Group generateGroup = new Group(buttonsGroup, SWT.NONE);
		generateGroup.setLayout(new GridLayout(2, true));
		generateGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Label incrementLabel = new Label(generateGroup, SWT.NONE);
		incrementLabel.setText("Increment between intervals(in hours)");
		incrementLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Spinner incrementSpinner = new Spinner(generateGroup, SWT.NONE);
		incrementSpinner.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		incrementSpinner.setMinimum(1);
		incrementSpinner.setMaximum(event.getDuration());
		incrementSpinner.setSelection(event.getDuration());
		incrementSpinner.setIncrement(1);
		fMediator.registerControl(incrementSpinner, "incrementSpinner");
		
		Button addButton = new Button(buttonsGroup, SWT.PUSH);
		addButton.setText("Add entry");
		addButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		fMediator.registerControl(addButton, "addButton");
		addButton.addSelectionListener(new DefaultSelectionListener(fMediator, addButton));
		
		Button generateButton = new Button(buttonsGroup, SWT.PUSH);
		generateButton.setText("Generate entries");
		generateButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		fMediator.registerControl(generateButton, "generateButton");
		generateButton.addSelectionListener(new DefaultSelectionListener(fMediator, generateButton));
		return main;
	}

	private void buildTableItems(Table table, Event event) {
		List<EventInterval> eventIntervals = event.getIntervals();
		for (EventInterval interval : eventIntervals) {
			TableItem item = new TableItem(table, SWT.NONE);
			String date = interval.getDate().toString();
			item.setText(new String[] { date, ""+interval.getStartHour(), ""+interval.getEndHour(),
										"" +  interval.getVotes(VoteType.YES),
										"" +  interval.getVotes(VoteType.NO), 
										"" +  interval.getVotes(VoteType.IFB) });
		}
	}

	private Control createEventInformationTab(TabFolder tabFolder, Event event) {
		Composite main = new Composite(tabFolder, SWT.NONE);
		main.setLayout(new GridLayout(1, false));

		Group mainGroup = new Group(main, SWT.NONE);
		mainGroup.setLayout(new GridLayout(2, true));
		mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Label eventNameLabel = new Label(mainGroup, SWT.NONE);
		eventNameLabel.setText("Event name:");
		eventNameLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Text eventNameText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		eventNameText.setText(event.getName());
		eventNameText.setTextLimit(100);
		eventNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label minDateLabel = new Label(mainGroup, SWT.NONE);
		minDateLabel.setText("Earliest date:");
		minDateLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		DateTime minDateTime = new DateTime(mainGroup, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
		Date eventMinDate = event.getMinStartDate();
		minDateTime.setDate(eventMinDate.getDay(), eventMinDate.getMonth(), eventMinDate.getDay());
		minDateTime.setEnabled(false);
		minDateTime.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label maxDateLabel = new Label(mainGroup, SWT.NONE);
		maxDateLabel.setText("Latest date:");
		maxDateLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		DateTime maxDateTime = new DateTime(mainGroup, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
		Date eventMaxDate = event.getMaxEndDate();
		maxDateTime.setDate(eventMaxDate.getDay(), eventMaxDate.getMonth(), eventMaxDate.getDay());
		maxDateTime.setEnabled(false);
		maxDateTime.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label durationLabel = new Label(mainGroup, SWT.NONE);
		durationLabel.setText("Duration:");
		durationLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Text durationText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		durationText.setText("" + event.getDuration());
		durationText.setTextLimit(100);
		durationText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label startHourLabel = new Label(mainGroup, SWT.NONE);
		startHourLabel.setText("Earliest start hour:");
		startHourLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Text startHourText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		startHourText.setText("" + event.getStartHour());
		startHourText.setTextLimit(100);
		startHourText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label endHourLabel = new Label(mainGroup, SWT.NONE);
		endHourLabel.setText("Latest ending hour:");
		endHourLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Text endHourText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		endHourText.setText("" + event.getEndHour());
		endHourText.setTextLimit(100);
		endHourText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return main;
	}

	private Control createActivityLogTab(TabFolder tabFolder, Event event) {
		Button button = new Button (tabFolder, SWT.PUSH);
		button.setText ("Activity log page");
		return button;
	}
}
