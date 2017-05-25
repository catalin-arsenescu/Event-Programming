package com.eventprogramming.gui.logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.eventprogramming.client.ClientConnection;
import com.eventprogramming.client.ClientGUI;
import com.eventprogramming.client.ClientGUI.STATE;
import com.eventprogramming.utils.Utils;

public class HomePageMediator extends AbstractPageMediator {

	private static String[] identifiers = { "createEventButton", "joinEventButton", "eventAdminButton" };

	public HomePageMediator(ClientGUI clientGUI, ClientConnection clientConnection) {
		super(clientGUI, clientConnection);
	}

	@Override
	public void notifyEvent(Control control, SelectionEvent event) {
		String identifier = getIdentifier(control);
		
		if ("createEventButton".equals(identifier)) {
			createEvent();
		} else if ("joinEventButton".equals(identifier)) {
			joinEvent();
		} else if ("eventAdminButton".equals(identifier)) {
			eventAdmin();
		}
	}

	public void createEvent() {
		fClientGUI.switchState(STATE.CREATE_EVENT);
	}
	
	public void joinEvent() {
		fClientGUI.switchState(STATE.JOIN_EVENT);
	}
	
	public void eventAdmin() {
		fClientGUI.switchState(STATE.EVENT_ADMINISTRATION);		
	}
	
	@Override
	protected Set<String> initializeIdentifiers() {
		return new HashSet<>(Arrays.asList(identifiers));
	}

}
