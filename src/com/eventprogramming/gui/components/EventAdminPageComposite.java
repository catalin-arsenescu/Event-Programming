package com.eventprogramming.gui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.eventprogramming.event.Event;
import com.eventprogramming.gui.logic.DefaultSelectionListener;
import com.eventprogramming.gui.logic.PageMediator;

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
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);
		String[] titles = {"Date", "Starting hour", "YES Votes", "NO Votes", "IFNEEDBE Votes"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText(titles[i]);
		}
		int count = 1;
		for (int i=0; i<count; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (0, "WIP");
			item.setText (1, "WIP");
			item.setText (2, "WIP");
			item.setText (3, "WIP");
			item.setText (4, "WIP");
		}
		for (int i=0; i<titles.length; i++) {
			table.getColumn(i).pack();
		}

		Group buttonsGroup = new Group(main, SWT.NONE);
		buttonsGroup.setLayout(new GridLayout(2, true));
		buttonsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Button addButton = new Button(buttonsGroup, SWT.PUSH);
		addButton.setText("Add entry");
		addButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		addButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			
		});
		
		Button generateButton = new Button(buttonsGroup, SWT.PUSH);
		generateButton.setText("Generate entries");
		generateButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		return main;
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
		
		return main;
	}

	private Control createActivityLogTab(TabFolder tabFolder, Event event) {
		Button button = new Button (tabFolder, SWT.PUSH);
		button.setText ("Activity log page");
		return button;
	}
}
