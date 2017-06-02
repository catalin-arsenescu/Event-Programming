package com.eventprogramming.widgets;

import java.util.ArrayList;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.eventprogramming.client.ClientConnection;
import com.eventprogramming.utils.Utils;

public class EventAdministrationCombo extends Combo {

	private static final String VOTES = "Votes";
	private static final String ACTIVITY_LOG = "Activity log";
	private static final String MANAGE_POLLS = "Manage polls";
	private static final String EVENT_INFORMATION = "Event Information";

	private class EventAdminSelectionListener implements SelectionListener {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (getSelectionIndex() == lastSelectedItemIndex)
				return;
			
			if (tabFolder.getVisible()) {
				boolean okPressed = Utils.openDialog(shell, "Warning", 
						"Switching to a new event will not save any modifications made after your last save. "
						+ "If you have unsaved notifications cancel this dialog and come back when you saved them!",
						() -> {},
						() -> {});
				
				if (!okPressed) {
					undoSelection();
					return;
				}
			}
			
			lastSelectedItemIndex = getSelectionIndex();
			
			if (!tabFolder.getVisible())
				tabFolder.setVisible(true);
			
			buildTabComposites();
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	}
	
	private static final String[] tabItemNames = new String[] {EVENT_INFORMATION, MANAGE_POLLS, ACTIVITY_LOG, VOTES};
	private String[] eventNames;
	private TabFolder tabFolder;
	private int lastSelectedItemIndex;
	private Shell shell;
	private ArrayList<TabItem> tabItems;
	private ClientConnection clientConnection;

	public EventAdministrationCombo(Composite parent, int style, Shell shell, ClientConnection clientConnection, String[] eventNames) {
		super(parent, style);
		this.eventNames = eventNames;
		this.shell = shell;
		this.clientConnection = clientConnection;
		lastSelectedItemIndex = -1;
		setItems(eventNames);
		addSelectionListener(new EventAdminSelectionListener());
	}

	private void initializeTabComposites() {
		tabItems = new ArrayList<>();
		for (String tabName : tabItemNames) {
			TabItem item = new TabItem(tabFolder, SWT.NONE);
			item.setText(tabName);
			tabItems.add(item);
		}
	}

	public void buildTabComposites() {

		for (TabItem item : tabItems)
			item.setControl(createTabItem(item.getText()));
	}

	private Control createTabItem(String tabName) {
		switch(tabName) {
		case ACTIVITY_LOG:
			return createActivityLogTab();
		case EVENT_INFORMATION:
			return createEventInformationTab();
		case MANAGE_POLLS:
			return createManagePollsTab();
		case VOTES:
			return createVotesTab();
		}
		
		return null;
	}
	
	private Control createVotesTab() {
		Button button = new Button (tabFolder, SWT.PUSH);
		button.setText ("Page " + getText());
		return button;
	}

	private Control createManagePollsTab() {
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

	private Control createEventInformationTab() {
		Composite main = new Composite(tabFolder, SWT.NONE);
		main.setLayout(new GridLayout(1, false));

		Group mainGroup = new Group(main, SWT.NONE);
		mainGroup.setLayout(new GridLayout(2, true));
		mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Label eventNameLabel = new Label(mainGroup, SWT.NONE);
		eventNameLabel.setText("Event name:");
		eventNameLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Text eventNameText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		eventNameText.setText("Event Programming Opening Ceremony");
		eventNameText.setTextLimit(100);
		eventNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		return main;
	}

	private Control createActivityLogTab() {
		Button button = new Button (tabFolder, SWT.PUSH);
		button.setText ("Page " + getText());
		return button;
	}

	private void undoSelection() {
		if (lastSelectedItemIndex < 0)
			return;
		
		select(lastSelectedItemIndex);
	}
	
	public void setTabFolder(TabFolder tabFolder) {
		this.tabFolder = tabFolder;
		initializeTabComposites();
	}
	
	@Override
	protected void checkSubclass() {}
}
