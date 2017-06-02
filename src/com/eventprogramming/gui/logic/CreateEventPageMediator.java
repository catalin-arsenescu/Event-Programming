package com.eventprogramming.gui.logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import com.eventprogramming.client.ClientConnection;
import com.eventprogramming.client.ClientGUI;
import com.eventprogramming.client.ClientGUI.STATE;
import com.eventprogramming.constants.Constants;
import com.eventprogramming.event.Event;
import com.eventprogramming.utils.Utils;

public class CreateEventPageMediator extends AbstractPageMediator {

	private static String[] identifiers = { "eventNameText", "greedyTypeButton", "freeTypeButton", "startDateTime",
											"endDateTime", "durationSpinner", "startHourSpinner", "endHourSpinner",
											"buttonCreateEvent" };

	public CreateEventPageMediator(ClientGUI clientGUI, ClientConnection clientConnection) {
		super(clientGUI, clientConnection);
	}

	@Override
	public void notifyEvent(Control control, SelectionEvent event) {
		String identifier = getIdentifier(control);
		if ("buttonCreateEvent".equals(identifier)) {
			createEvent();
		}
	}

	private void createEvent() {
		if (checkInput()) {
			String sessionUsername = fClientGUI.getUsername();
			Text eventNameText = (Text) getControl("eventNameText");
			Button greedyTypeButton = (Button) getControl("greedyTypeButton");
			DateTime startDateTime = (DateTime) getControl("startDateTime");
			DateTime endDateTime = (DateTime) getControl("endDateTime");
			Spinner durationSpinner = (Spinner) getControl("durationSpinner");
			Spinner startHourSpinner = (Spinner) getControl("startHourSpinner");
			Spinner endHourSpinner = (Spinner) getControl("endHourSpinner");
			String createEventOk = fClientConnection.sendNewEvent(
								eventNameText.getText(),
								greedyTypeButton.getSelection(),
								startDateTime,
								endDateTime,
								startHourSpinner.getSelection(),
								endHourSpinner.getSelection(),
								durationSpinner.getSelection(),
								sessionUsername);
			if (!Constants.SERVER_ERROR.equals(createEventOk)) {
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
				fClientGUI.getEventCache().addEvent(event);
				
				Utils.openDialog(fClientGUI.getShell(), "Success",
						"Event was successfully created! However you still need to: \n" +
						"a) Generate or manually create event intervals for others to vote \n" + 
						"b) Share the event code to the participants so they can vote \n" +
						"These can be done from the 'Event administration page'. You will be redirected there now!",
						() -> fClientGUI.switchState(STATE.EVENT_ADMINISTRATION),
						() -> fClientGUI.switchState(STATE.HOMEPAGE));
			} else {
				Utils.openDialog(fClientGUI.getShell(), "Create event ERROR",
						Constants.SERVER_OFFLINE_ERROR,
						() -> {},
						() -> {});
			}
		} else {
			Utils.openDialog(fClientGUI.getShell(), "Create event ERROR",
					"There was an error creating the event! "
					+ "Double check your input is consistent(e.g. startDate is before endDate",
					() -> {},
					() -> {});
		}
	}
	
	private boolean checkInput() {
		Text eventNameText = (Text) getControl("eventNameText");
		Button greedyTypeButton = (Button) getControl("greedyTypeButton");
		Button freeTypeButton = (Button) getControl("freeTypeButton");

		if (eventNameText.getText().isEmpty())
			return false;
		
		if (!greedyTypeButton.getSelection() && !freeTypeButton.getSelection())
			return false;
		
		return true;
	}
	
	@Override
	protected Set<String> initializeIdentifiers() {
		return new HashSet<>(Arrays.asList(identifiers));
	}

}
