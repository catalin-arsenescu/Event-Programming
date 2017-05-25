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
import com.eventprogramming.gui.components.CreateEventPageComposite;
import com.eventprogramming.gui.components.EventAdminPageComposite;
import com.eventprogramming.gui.components.HomePageComposite;
import com.eventprogramming.gui.components.RegisterPageComposite;
import com.eventprogramming.gui.components.WelcomePageComposite;
import com.eventprogramming.gui.logic.CreateEventPageMediator;
import com.eventprogramming.gui.logic.EventAdminPageMediator;
import com.eventprogramming.gui.logic.HomePageMediator;
import com.eventprogramming.gui.logic.JoinEventPageMediator;
import com.eventprogramming.gui.logic.PageMediator;
import com.eventprogramming.gui.logic.RegisterPageMediator;
import com.eventprogramming.gui.logic.WelcomePageMediator;
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
		WELCOME, REGISTER, HOMEPAGE, CREATE_EVENT, JOIN_EVENT, EVENT_ADMINISTRATION
	}

	private ClientConnection fClientConnection;

	private final Display fDisplay;
	private final Shell fShell;
	private final Composite fMainComposite;
	private final StackLayout fLayout;

	private Composite fWelcomePage;
	private Composite fRegisterPage;
	private Composite fHomePage;
	private Composite fCreateEventPage;
	private Composite fJoinEventPage;
	private Composite fEventAdminPage;
	
	public EventCache fEventCache;

	private STATE fState;

	public String sessionUsername = null;

	private PageMediator fWelcomeMediator;
	private PageMediator fRegisterMediator;
	private PageMediator fHomePageMediator;
	private PageMediator fCreateEventMediator;
	private PageMediator fJoinEventMediator;
	private PageMediator fEventAdminMediator;
	
	public ClientGUI(/* boolean readCredentialsFromSecureStorage */) {
		initializeNonGui();

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

	public void switchState(STATE state) {
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
		case HOMEPAGE:
			fLayout.topControl = fHomePage;
			boundsForState = getBoundsForState(STATE.HOMEPAGE);
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
		case HOMEPAGE:
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
		fEventAdminPage = new EventAdminPageComposite(fMainComposite, SWT.BORDER, 
				new EventAdminPageMediator(this, fClientConnection), fEventCache.toNameArray());
	}

	private void initializeCreateEventPage() {
		fCreateEventPage = new CreateEventPageComposite(fMainComposite, SWT.BORDER, fCreateEventMediator);
	}
	
	private void initializeJoinEventPage() {
		
	}

	private void initializeEventPage() {
		fHomePage = new HomePageComposite(fMainComposite, SWT.BORDER, fHomePageMediator);
	}

	private void initializeRegisterPage() {
		fRegisterPage = new RegisterPageComposite(fMainComposite, SWT.BORDER, fRegisterMediator);
	}

	private void initializeWelcomePage() {
		fWelcomePage = new WelcomePageComposite(fMainComposite, SWT.BORDER, fWelcomeMediator);
	}

	private void initializeNonGui() {
		fClientConnection = new ClientConnection(this);
		fWelcomeMediator = new WelcomePageMediator(this, fClientConnection);
		fRegisterMediator = new RegisterPageMediator(this, fClientConnection);
		fHomePageMediator = new HomePageMediator(this, fClientConnection);
		fCreateEventMediator = new CreateEventPageMediator(this, fClientConnection);
		fJoinEventMediator = new JoinEventPageMediator(this, fClientConnection);
		fEventAdminMediator = new EventAdminPageMediator(this, fClientConnection);
		fEventCache = new EventCache();
	}

	public void reportError(String errorMessage) {
		MessageBox messageBox = new MessageBox(fShell);
		messageBox.setMessage(errorMessage);
		messageBox.open();
	}

	public Shell getShell() {
		return fShell;
	}
	
	public static void main(String[] args) {
		new ClientGUI();
	}

	public void setUsername(String text) {
		sessionUsername = text;
	}
	
	public String getUsername() {
		return sessionUsername;
	}

	public EventCache getEventCache() {
		return fEventCache;
	}
}
