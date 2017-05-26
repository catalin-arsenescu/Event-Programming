package com.eventprogramming.gui.logic;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

import com.eventprogramming.client.ClientConnection;
import com.eventprogramming.client.ClientGUI;
import com.eventprogramming.client.ClientGUI.STATE;
import com.eventprogramming.event.Event;
import com.eventprogramming.gui.components.EventAdminPageComposite;
import com.eventprogramming.utils.Utils;

public class EventAdminPageMediator extends AbstractPageMediator {

	private static String[] identifiers = { "combo", "tabFolder", "page-composite", "dateTime",
											"startHourSpinner", "incrementSpinner", "addButton", "generateButton" };
	
	private int lastSelectedItemIndex;

	public EventAdminPageMediator(ClientGUI clientGUI, ClientConnection clientConnection) {
		super(clientGUI, clientConnection);
		lastSelectedItemIndex = -1;
	}

	@Override
	public void notifyEvent(Control control, SelectionEvent event) {
		String identifier = getIdentifier(control);
		if ("combo".equals(identifier)) {
			combo();
		} else if ("addButton".equals(identifier)) {
			addEvent();
		} else if ("generateButton".equals(identifier)) {
			generateEvents();
		}
	}

	
	private void addEvent() {
		checkNewEventInput();
		DateTime dateTime = (DateTime) getControl("dateTime");
		Spinner spinner = (Spinner) getControl("startHourSpinner");
		Event selectedEvent = getSelectedEvent();
		int endHour = spinner.getSelection() + selectedEvent.getDuration();
		if (endHour > selectedEvent.getEndHour())
			endHour = selectedEvent.getEndHour();
		fClientConnection.sendNewEventInterval(selectedEvent.getEventCode(), 
											dateTime, spinner.getSelection(),
											endHour);
	}

	private boolean checkNewEventInput() {
		DateTime dateTime = (DateTime) getControl("dateTime");
		Event event = getSelectedEvent();
		
		Date tmp = Utils.getDateFromDateTime(dateTime);
		return Utils.checkDateWithDateInterval(event.getMinStartDate(), tmp, event.getMaxEndDate());
	}

	private void generateEvents() {
		Spinner spinner = (Spinner) getControl("incrementSpinner");
		int hourIncrement = spinner.getSelection();
		Event event = getSelectedEvent();
		Date startDate = event.getMinStartDate();
		Date endDate = event.getMaxEndDate();
		String eventCode = event.getEventCode();
		int duration = event.getDuration();
		int eventEndHour = event.getEndHour();
		int eventStartHour = event.getStartHour();
		
		LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		for (LocalDate date = start; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
			int startHour = eventStartHour;
			while (startHour + duration <= eventEndHour) {
				fClientConnection.sendNewEventInterval(eventCode, 
						date.toString(), startHour,
						startHour + duration);
				startHour += hourIncrement;
			}
		}
	}

	private void combo() {
		Combo combo = (Combo) getControl("combo");
		TabFolder tabFolder = (TabFolder) getControl("tabFolder");
		Shell shell = fClientGUI.getShell();
		
		if (combo.getSelectionIndex() == lastSelectedItemIndex)
			return;
		
		if (lastSelectedItemIndex >= 0) {
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
		
		lastSelectedItemIndex = combo.getSelectionIndex();

		EventAdminPageComposite pageComposite = (EventAdminPageComposite) getControl("page-composite");
		pageComposite.buildTabComposites(tabFolder, getSelectedEvent());
	}

	private Event getSelectedEvent() {
		Combo combo = (Combo) getControl("combo");
		String eventName = combo.getItem(lastSelectedItemIndex);
		return fClientGUI.fEventCache.getByName(eventName);
	}

	private void undoSelection() {
		if (lastSelectedItemIndex < 0)
			return;

		Combo combo = (Combo) getControl("combo");
		combo.select(lastSelectedItemIndex);
	}
	
	@Override
	protected Set<String> initializeIdentifiers() {
		return new HashSet<>(Arrays.asList(identifiers));
	}

}
