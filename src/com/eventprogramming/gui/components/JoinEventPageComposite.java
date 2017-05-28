package com.eventprogramming.gui.components;

import java.util.Date;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.eventprogramming.event.Event;
import com.eventprogramming.event.EventInterval;
import com.eventprogramming.gui.logic.DefaultSelectionListener;
import com.eventprogramming.gui.logic.PageMediator;
import com.eventprogramming.widgets.EventIntervalVoteButton;
import com.eventprogramming.widgets.EventIntervalVoteButton.EventType;

public class JoinEventPageComposite extends Composite {

	private static final String EVENT_INFORMATION = "Event Information";
	private static final String MAIN_POLL = "Event Poll";
	private static final String VOTE_INFORMATION = "Votes Information";
	private PageMediator fMediator;
	private Event fEvent;

	public JoinEventPageComposite(Composite parent, int style, PageMediator mediator, Event event) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		fEvent = event;
		fMediator = mediator;
		buildComposite();
	}

	private void buildComposite() {
		Group eventSelectGroup = new Group(this, SWT.NONE);
		eventSelectGroup.setLayout(new GridLayout(1, true));
		eventSelectGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		TabFolder tabFolder = new TabFolder(this, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setVisible(true);
		fMediator.registerControl(tabFolder, "tabFolder");

		buildTabComposites(tabFolder, fEvent);
	}

	private void buildTabComposites(TabFolder tabFolder, Event event) {
		if (tabFolder.getItemCount() <= 0) {
			TabItem item = new TabItem(tabFolder, SWT.NONE);
			item.setText(EVENT_INFORMATION);
			item.setControl(createEventInformationTab(tabFolder, event));

			item = new TabItem(tabFolder, SWT.NONE);
			item.setText(MAIN_POLL);
			item.setControl(createMainPollTab(tabFolder, event));

			item = new TabItem(tabFolder, SWT.NONE);
			item.setText(VOTE_INFORMATION);
			item.setControl(createMainPollTab(tabFolder, event));

		} else {
			TabItem[] items = tabFolder.getItems();
			for (TabItem item : items) {
				if (item.getText().equals(EVENT_INFORMATION))
					item.setControl(createEventInformationTab(tabFolder, event));
				if (item.getText().equals(MAIN_POLL))
					item.setControl(createMainPollTab(tabFolder, event));
				if (item.getText().equals(VOTE_INFORMATION))
					item.setControl(createVoteInformationTab(tabFolder, event));
			}
		}
	}

	private Control createVoteInformationTab(TabFolder tabFolder, Event event) {
		// TODO Auto-generated method stub
		return null;
	}

	private Control createMainPollTab(TabFolder tabFolder, Event event) {
		Composite main = new Composite(tabFolder, SWT.NONE);
		main.setLayout(new GridLayout(1, false));

		Group mainGroup = new Group(main, SWT.NONE);
		mainGroup.setLayout(new GridLayout(1, true));
		mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Table table = new Table(mainGroup, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
		data.heightHint = 200;
		table.setLayoutData(data);
		String[] titles = { "Date", "Starting hour", "Ending hour", "YES Votes", "NO Votes", "IFNEEDBE Votes" };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
		}
		buildTableItems(table, event);
		for (int i = 0; i < titles.length; i++) {
			table.getColumn(i).pack();
		}

		Button saveButton = new Button(mainGroup, SWT.PUSH);
		saveButton.setText("Save");
		GridData layoutData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		layoutData.widthHint = 60;
		saveButton.setLayoutData(layoutData);
		fMediator.registerControl(saveButton, "addButton");
		saveButton.addSelectionListener(new DefaultSelectionListener(fMediator, saveButton));

		return main;
	}

	private void buildTableItems(Table table, Event event) {
		List<EventInterval> eventIntervals = event.getIntervals();
		int eventsNo = eventIntervals.size();
		
		for (int i = 0; i < eventsNo; i++) {
			new TableItem(table, SWT.NONE);
		}
		
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			TableItem item = items[i];
			EventInterval interval = eventIntervals.get(i);
			
			String date = interval.getDate().toString();
			item.setText(0, date);
			item.setText(1, "" + interval.getStartHour());
			item.setText(2, "" + interval.getEndHour());
			
			TableEditor editor;
			EventIntervalVoteButton yesButton = new EventIntervalVoteButton(table, interval, EventType.YES);
			editor = new TableEditor(table);
			editor.grabHorizontal = true;
			editor.setEditor(yesButton, items[i], 3);
			
			EventIntervalVoteButton noButton = new EventIntervalVoteButton(table, interval, EventType.NO);
			editor = new TableEditor(table);
			editor.grabHorizontal = true;
			editor.setEditor(noButton, items[i], 4);
			
			EventIntervalVoteButton ifbButton = new EventIntervalVoteButton(table, interval, EventType.IFB);
			editor = new TableEditor(table);
			editor.grabHorizontal = true;
			editor.setEditor(ifbButton, items[i], 5);
			
			yesButton.setOthers(noButton, ifbButton);
			noButton.setOthers(yesButton, ifbButton);
			ifbButton.setOthers(yesButton, noButton);
			
		}
//		for (EventInterval interval : eventIntervals) {
//			TableItem item = new TableItem(table, SWT.NONE);
//			String date = interval.getDate().toString();
//			item.setText(new String[] { date, "" + interval.getStartHour(), "" + interval.getEndHour(), "" + 0, "" + 0,
//					"" + 0 });
//		}
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
		eventNameText.setEditable(false);

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
		durationText.setEditable(false);

		Label startHourLabel = new Label(mainGroup, SWT.NONE);
		startHourLabel.setText("Earliest start hour:");
		startHourLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Text startHourText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		startHourText.setText("" + event.getStartHour());
		startHourText.setTextLimit(100);
		startHourText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		startHourText.setEditable(false);

		Label endHourLabel = new Label(mainGroup, SWT.NONE);
		endHourLabel.setText("Latest ending hour:");
		endHourLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Text endHourText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		endHourText.setText("" + event.getEndHour());
		endHourText.setTextLimit(100);
		endHourText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		endHourText.setEditable(false);
		return main;
	}
}
