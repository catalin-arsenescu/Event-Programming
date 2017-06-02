package com.eventprogramming.gui.logic;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.eventprogramming.client.ClientConnection;
import com.eventprogramming.client.ClientGUI;
import com.eventprogramming.client.ClientGUI.STATE;
import com.eventprogramming.constants.Constants;
import com.eventprogramming.event.Event;
import com.eventprogramming.event.IntervalVote.VoteType;
import com.eventprogramming.gui.components.EventAdminPageComposite;
import com.eventprogramming.utils.Utils;

public class EventAdminPageMediator extends AbstractPageMediator {

	private static String[] identifiers = { "combo", "tabFolder", "page-composite", "dateTime",
											"startHourSpinner", "incrementSpinner", "addButton", 
											"generateButton", "userPriorityText", "addPriorityButton",
											"prioritiesList", "prioritiesTable", "intervalsTable" };
	
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
		} else if ("addPriorityButton".equals(identifier)) {
			addPriority();
		}
	}

	
	private void addPriority() {
		if (!checkPriorityInput())
			return;

		String username = ((Text) getControl("userPriorityText")).getText();
		int priority = ((org.eclipse.swt.widgets.List) getControl("prioritiesList")).getSelectionIndex() + 1;
		String eventCode = getSelectedEvent().getEventCode();
		
		String errorCode = fClientConnection.addPriority(eventCode, username, priority);
		if (!"OK".equals(errorCode))
			return;
		
		insertPriorityInTable(username, priority);
	}

	private boolean checkPriorityInput() {
		Text text = (Text) getControl("userPriorityText");
		if (text == null || text.getText().isEmpty())
			return false;
		
		return true;
	}

	private void addEvent() {
		checkNewEventInput();
		DateTime dateTime = (DateTime) getControl("dateTime");
		Spinner spinner = (Spinner) getControl("startHourSpinner");
		Event selectedEvent = getSelectedEvent();
		int endHour = spinner.getSelection() + selectedEvent.getDuration();
		if (endHour > selectedEvent.getEndHour())
			endHour = selectedEvent.getEndHour();
		String response = fClientConnection.sendNewEventInterval(selectedEvent.getEventCode(), 
											dateTime, spinner.getSelection(),
											endHour);
		if (Constants.SERVER_ERROR.equals(response))
			return;
		
		addIntervalInTable(Utils.getDateString(dateTime), spinner.getSelection(), endHour);
	}

	private void addIntervalInTable(String dateTime, int startHour, int endHour) {
		
		Table intervalsTable = (Table) getControl("intervalsTable");
		TableItem item = new TableItem(intervalsTable, SWT.NONE);
		item.setText(new String[] { dateTime,
									"" + startHour,
									"" + endHour, "" + 0, "" + 0, "" + 0, "" + 0 });
	}

	private void insertPriorityInTable(String username, int priority) {

		Table prioritiesTable = (Table) getControl("prioritiesTable");
		TableItem[] items = prioritiesTable.getItems();
		for (TableItem item : items)
			if (username.equals(item.getText(0))) {
				item.setText(1, "" + priority);
				return;
			}
		
		TableItem item = new TableItem(prioritiesTable, SWT.NONE);
		item.setText(new String[] { username, "" + priority });
	}
	
	private void clearIntervalTable() {
		Table intervalTable = (Table) getControl("intervalsTable");
		intervalTable.clearAll();
		intervalTable.setItemCount(0);
		intervalTable.setTopIndex(0);
	}
	
	private boolean checkNewEventInput() {
		DateTime dateTime = (DateTime) getControl("dateTime");
		Event event = getSelectedEvent();
		
		Date tmp = Utils.getDateFromDateTime(dateTime);
		return Utils.checkDateWithDateInterval(event.getMinStartDate(), tmp, event.getMaxEndDate());
	}

	private void generateEvents() {
		boolean confirmed = Utils.openDialog(fClientGUI.getShell(), "Confirm", 
				"Generating events will delete any existing ones and their associated votes.\n" + 
				"Are you sure you want to do this?",
				() -> {},
				() -> {});
		
		if (!confirmed)
			return;
		
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

		fClientConnection.deleteEventIntervals(eventCode);
		clearIntervalTable();
		for (LocalDate date = start; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
			int startHour = eventStartHour;
			while (startHour + duration <= eventEndHour) {
				fClientConnection.sendNewEventInterval(eventCode, 
						date.toString(), startHour,
						startHour + duration);
				addIntervalInTable(date.toString(), startHour, startHour + duration);
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
		Event selectedEvent = getSelectedEvent();
		fClientConnection.getEventIntervals(selectedEvent);
		fClientConnection.getVotesForEvent(selectedEvent);
		fClientConnection.getPrioritiesForEvent(selectedEvent);
		pageComposite.buildTabComposites(tabFolder, selectedEvent);
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
