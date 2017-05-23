package com.eventprogramming.client;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.eventprogramming.constants.Constants;
import com.eventprogramming.event.Event;
import com.eventprogramming.event.EventCache;
import com.eventprogramming.utils.Utils;
import com.eventprogramming.widgets.EventAdministrationCombo;

/**
 * Entry point for the user application Will start the graphical user interface
 * TODO: Add CLI arguments TODO: Expand states
 * 
 * @author Catalin
 */
public class ClientGUI {
	
	public enum STATE {
		WELCOME, REGISTER, EVENT, CREATE_EVENT, JOIN_EVENT, EVENT_ADMINISTRATION
	}

	private ClientConnection fClientConnection;

	private final Display fDisplay;
	private final Shell fShell;
	private final Composite fMainComposite;
	private final StackLayout fLayout;

	private Composite fWelcomePage;
	private Composite fRegisterPage;
	private Composite fEventPage;
	private Composite fCreateEventPage;
	private Composite fJoinEventPage;
	private Composite fEventAdminPage;
	
	public final EventCache fEventCache;

	private STATE fState;

	private String sessionUsername = null;

	
	public ClientGUI(/* boolean readCredentialsFromSecureStorage */) {
		initializeNonGui();

		fEventCache = new EventCache();
		/* Initialize display, shell, and main composite */
		fDisplay = new Display();
		fShell = new Shell(fDisplay);
		fShell.setSize(new Point(1366, 720));
		fShell.setText(Constants.APP_WINDOW_TITLE);
		Rectangle clientArea = fShell.getClientArea();
		fMainComposite = new Composite(fShell, SWT.BORDER);
		fMainComposite.setBounds(clientArea.x + 10, clientArea.y + 10, clientArea.width - 20, clientArea.height - 20);
		fShell.setBackground(new Color(fDisplay, new RGB(6, 15, 125)));
		fLayout = new StackLayout();
		fMainComposite.setLayout(fLayout);

		initializeGui();
		switchState(STATE.WELCOME);

		fShell.open();
		while (!fShell.isDisposed()) {
			if (!fDisplay.readAndDispatch())
				fDisplay.sleep();
		}
		fDisplay.dispose();
	}

	private void switchState(STATE state) {
		if (state == null)
			return;
		fState = state;
		Rectangle boundsForState = fShell.getBounds();

		switch (state) {
		case WELCOME:
			fLayout.topControl = fWelcomePage;
			boundsForState = getBoundsForState(STATE.WELCOME);
			break;
		case REGISTER:
			fLayout.topControl = fRegisterPage;
			boundsForState = getBoundsForState(STATE.REGISTER);
			break;
		case EVENT:
			fLayout.topControl = fEventPage;
			boundsForState = getBoundsForState(STATE.EVENT);
			break;
		case CREATE_EVENT:
			fLayout.topControl = fCreateEventPage;
			boundsForState = getBoundsForState(STATE.CREATE_EVENT);
			break;
		case JOIN_EVENT:
			fLayout.topControl = fJoinEventPage;
			boundsForState = getBoundsForState(STATE.JOIN_EVENT);
			break;
		case EVENT_ADMINISTRATION:
			initializeEventAdministrationPage();
			fLayout.topControl = fEventAdminPage;
			boundsForState = getBoundsForState(STATE.EVENT_ADMINISTRATION);
			break;
		}

		fMainComposite.setBounds(boundsForState);
		fMainComposite.layout();
	}

	private Rectangle getBoundsForState(STATE state) {
		Rectangle clientArea = fShell.getClientArea();
		int x, y, width, height;

		switch (state) {
		case WELCOME:
		case REGISTER:
		case EVENT:
		case CREATE_EVENT:
		case JOIN_EVENT:
		case EVENT_ADMINISTRATION:
			width = clientArea.width / 2;
			height = clientArea.height / 2;
			x = width / 2;
			y = height / 2;
			return new Rectangle(x, y, width, height);
		}

		return clientArea;
	}

	/**
	 * Initialize all page composites
	 */
	private void initializeGui() {

		initializeWelcomePage();
		initializeRegisterPage();
		initializeEventPage();
		initializeCreateEventPage();
		initializeJoinEventPage();
		initializeEventAdministrationPage();

	}

	private void initializeEventAdministrationPage() {
		fEventAdminPage = new Composite(fMainComposite, SWT.NONE);
		fEventAdminPage.setLayout(new GridLayout(1, false));
		
		Group eventSelectGroup = new Group(fEventAdminPage, SWT.NONE);
		eventSelectGroup.setLayout(new GridLayout(2, true));
		eventSelectGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Label eventSelectTitle = new Label(eventSelectGroup, SWT.NONE);
		eventSelectTitle.setText("Select event:");
		eventSelectTitle.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		
		EventAdministrationCombo combo = new EventAdministrationCombo(eventSelectGroup, SWT.NONE, fShell, fEventCache.toNameArray());
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		TabFolder tabFolder = new TabFolder (fEventAdminPage, SWT.BORDER);
		combo.setTabFolder(tabFolder);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setVisible(false);

	}

	private void initializeCreateEventPage() {
		fCreateEventPage = new Composite(fMainComposite, SWT.NONE);
		fCreateEventPage.setLayout(new GridLayout(1, false));

		Group mainGroup = new Group(fCreateEventPage, SWT.NONE);
		mainGroup.setLayout(new GridLayout(2, true));
		mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		
		Label eventNameLabel = new Label(mainGroup, SWT.NONE);
		eventNameLabel.setText("Event name:");
		eventNameLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Text eventNameText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		eventNameText.setText("Event Programming Opening Ceremony");
		eventNameText.setTextLimit(100);
		eventNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
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
		
		Button freeTypeButton = new Button(eventTypeButtonsGroup, SWT.RADIO);
		freeTypeButton.setText("Free");
		freeTypeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label startDateLabel = new Label(mainGroup, SWT.NONE);
		startDateLabel.setText("Earliest starting date:");
		startDateLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		DateTime startDateTime = new DateTime(mainGroup, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
		startDateTime.setDate(2017, 7, 1);
		startDateTime.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label endDateLabel = new Label(mainGroup, SWT.NONE);
		endDateLabel.setText("Latest ending date:");
		endDateLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		DateTime endDateTime = new DateTime(mainGroup, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
		endDateTime.setDate(2017, 7, 13);
		endDateTime.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label durationLabel = new Label(mainGroup, SWT.NONE);
		durationLabel.setText("Duration:");
		durationLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Spinner durationSpinner = new Spinner(mainGroup, SWT.NONE);
		durationSpinner.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		durationSpinner.setMinimum(0);
		durationSpinner.setMaximum(23);
		durationSpinner.setSelection(4);
		durationSpinner.setIncrement(1);
		
		Label startHourLabel = new Label(mainGroup, SWT.NONE);
		startHourLabel.setText("Earliest start hour:");
		startHourLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Spinner startHourSpinner = new Spinner(mainGroup, SWT.NONE);
		startHourSpinner.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		startHourSpinner.setMinimum(0);
		startHourSpinner.setMaximum(23);
		startHourSpinner.setSelection(16);
		startHourSpinner.setIncrement(1);
		
		Label endHourLabel = new Label(mainGroup, SWT.NONE);
		endHourLabel.setText("Latest ending hour:");
		endHourLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Spinner endHourSpinner = new Spinner(mainGroup, SWT.NONE);
		endHourSpinner.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		endHourSpinner.setMinimum(0);
		endHourSpinner.setMaximum(24);
		endHourSpinner.setSelection(24);
		endHourSpinner.setIncrement(1);
		
		Button buttonCreateEvent = new Button(fCreateEventPage, SWT.PUSH);
		buttonCreateEvent.setText("Create Event");
		buttonCreateEvent.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		buttonCreateEvent.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (checkInput()) {
					String createEventOk = fClientConnection.sendNewEvent(
										eventNameText.getText(),
										greedyTypeButton.getSelection(),
										startDateTime,
										endDateTime,
										startHourSpinner.getSelection(),
										endHourSpinner.getSelection(),
										durationSpinner.getSelection(),
										sessionUsername);
					if (createEventOk != "ERROR") {
						// Save event in cache
						Event event = new Event(eventNameText.getText(),
								sessionUsername,
								greedyTypeButton.getSelection() ? 1 : 0,
								Utils.getDateFromDateTime(startDateTime),
								Utils.getDateFromDateTime(endDateTime),
								startHourSpinner.getSelection(),
								endHourSpinner.getSelection(),
								durationSpinner.getSelection(),
								createEventOk);
						fEventCache.addEvent(event);
						
						Utils.openDialog(fShell, "Success",
								"Event was successfully created! However you still need to: \n" +
								"a) Generate or manually create event intervals for others to vote \n" + 
								"b) Share the event code to the participants so they can vote \n" +
								"These can be done from the 'Event administration page'. You will be redirected there now!",
								() -> switchState(STATE.EVENT_ADMINISTRATION),
								() -> switchState(STATE.EVENT));
					} else {
						Utils.openDialog(fShell, "Create event ERROR",
								Constants.SERVER_OFFLINE_ERROR,
								() -> {},
								() -> {});
					}
				} else {
					Utils.openDialog(fShell, "Create event ERROR",
							"There was an error creating the event! "
							+ "Double check your input is consistent(e.g. startDate is before endDate",
							() -> {},
							() -> {});
				}
			}

			private boolean checkInput() {
				if (eventNameText.getText().isEmpty())
					return false;
				
				if (!greedyTypeButton.getSelection() && !freeTypeButton.getSelection())
					return false;
				
				return true;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

		});
	}
	
	private void initializeJoinEventPage() {
		
	}

	private void initializeEventPage() {
		fEventPage = new Composite(fMainComposite, SWT.NONE);

		fEventPage.setLayout(new GridLayout(1, false));
		
		Group mainGroup = new Group(fEventPage, SWT.NONE);
		mainGroup.setLayout(new GridLayout(2, true));
		mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

		Button createEventButton = new Button(mainGroup, SWT.NONE);
		createEventButton.setText("Create an Event");
		createEventButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createEventButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				switchState(STATE.CREATE_EVENT);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);				
			}
		});
		
		Button joinEventButton = new Button(mainGroup, SWT.NONE);
		joinEventButton.setText("Join an Event");
		joinEventButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		joinEventButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				switchState(STATE.JOIN_EVENT);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);				
			}
		});
		
		Button eventAdminButton = new Button(mainGroup, SWT.NONE);
		eventAdminButton.setText("Administrate your events");
		eventAdminButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		eventAdminButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				switchState(STATE.EVENT_ADMINISTRATION);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);				
			}
		});
		
	}

	private void initializeRegisterPage() {
		fRegisterPage = new Composite(fMainComposite, SWT.BORDER);

		fRegisterPage.setLayout(new GridLayout(1, false));
		Group mainGroup = new Group(fRegisterPage, SWT.NONE);
		mainGroup.setLayout(new GridLayout(2, true));
		mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

		Label userLabel = new Label(mainGroup, SWT.NONE);
		userLabel.setText("User name:");
		userLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Text userText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		userText.setText("");
		userText.setTextLimit(100);
		userText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label passLabel = new Label(mainGroup, SWT.NONE);
		passLabel.setText("Password:");
		passLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Text passText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		passText.setText("");
		passText.setTextLimit(100);
		passText.setEchoChar('*');
		passText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label emailLabel = new Label(mainGroup, SWT.NONE);
		emailLabel.setText("Email:");
		emailLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Text emailText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		emailText.setText("");
		emailText.setTextLimit(100);
		emailText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button buttonRegister = new Button(fRegisterPage, SWT.PUSH);
		buttonRegister.setText("Register");
		buttonRegister.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		buttonRegister.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (checkInput()) {
					boolean registerDone = fClientConnection.sendNewUserCredentials(userText.getText(),
							passText.getText(), emailText.getText());
//					fClientConnection.sendHello();
					if (registerDone)
						switchState(STATE.WELCOME);
				} else
					openDialog();
			}

			private void openDialog() {
				// TODO Auto-generated method stub
			}

			private boolean checkInput() {
				// TODO Do it better
				if (userText.getText().isEmpty())
					return false;

				if (passText.getText().isEmpty())
					return false;

				if (emailText.getText().isEmpty())
					return false;

				return true;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});
	}

	private void initializeWelcomePage() {
		fWelcomePage = new Composite(fMainComposite, SWT.BORDER);

		fWelcomePage.setLayout(new GridLayout(1, false));
		Group mainGroup = new Group(fWelcomePage, SWT.NONE);
		mainGroup.setLayout(new GridLayout(2, true));
		mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

		Label userLabel = new Label(mainGroup, SWT.NONE);
		userLabel.setText("User name:");
		userLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));

		Text userText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		userText.setText("admin");
		userText.setTextLimit(100);
		userText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label passLabel = new Label(mainGroup, SWT.NONE);
		passLabel.setText("Password:");
		passLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));

		Text passText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		passText.setText("admin");
		passText.setTextLimit(100);
		passText.setEchoChar('*');
		passText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button buttonRegister = new Button(mainGroup, SWT.PUSH);
		buttonRegister.setText("Register");
		buttonRegister.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		buttonRegister.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				switchState(STATE.REGISTER);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		Button buttonLogin = new Button(mainGroup, SWT.PUSH);
		buttonLogin.setText("Login");
		buttonLogin.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		buttonLogin.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (checkInput()) {
					boolean loginOK = fClientConnection.sendLogin(userText.getText(), passText.getText());
					if (loginOK) {
						sessionUsername = userText.getText();
						fEventCache.setUsername(sessionUsername);
						switchState(STATE.EVENT);
					}	
					else
						openDialog(/* Error message */);
				} else {
					openDialog(/* Error message */);
				}
			}
			
			private void openDialog() {
				// TODO Auto-generated method stub
			}

			private boolean checkInput() {
				// TODO Do it better
				if (userText.getText().isEmpty())
					return false;

				if (passText.getText().isEmpty())
					return false;

				return true;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void initializeNonGui() {
		fClientConnection = new ClientConnection(this);
	}

	public void reportError(String errorMessage) {
		MessageBox messageBox = new MessageBox(fShell);
		messageBox.setMessage(errorMessage);
		messageBox.open();
	}

	public static void main(String[] args) {
		new ClientGUI();
	}
}
