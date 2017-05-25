package com.eventprogramming.gui.logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

import com.eventprogramming.client.ClientConnection;
import com.eventprogramming.client.ClientGUI;
import com.eventprogramming.client.ClientGUI.STATE;
import com.eventprogramming.event.Event;
import com.eventprogramming.gui.components.EventAdminPageComposite;
import com.eventprogramming.utils.Utils;

public class EventAdminPageMediator extends AbstractPageMediator {

	private static String[] identifiers = { "combo", "tabFolder", "page-composite" };
	
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
