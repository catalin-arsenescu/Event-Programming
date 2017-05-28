package com.eventprogramming.gui.logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.eventprogramming.client.ClientConnection;
import com.eventprogramming.client.ClientGUI;
import com.eventprogramming.client.ClientGUI.STATE;
import com.eventprogramming.event.Event;
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
		InputDialog dlg = new InputDialog(fClientGUI.getShell(), "Join an event", "Enter the event code:", "689141376", new IInputValidator() {

			@Override
			public String isValid(String arg0) {
				return null;
			}

		});
		if (dlg.open() == Window.OK) {
			String eventJson = fClientConnection.getEventForCode(dlg.getValue());
			if ("ERROR".equals(eventJson))
				return;

			JSONObject json;
			try {
				json = (JSONObject) new JSONParser().parse(eventJson);
				Event event = fClientGUI.getEventCache().parseEvent(json);
				fClientConnection.getEventIntervals(event);
				fClientGUI.initializeJoinEventPage(event);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
