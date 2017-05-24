package com.eventprogramming.gui.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;

import com.eventprogramming.client.ClientConnection;
import com.eventprogramming.client.ClientGUI;

public abstract class AbstractPageMediator implements PageMediator {

	protected Map<Control, String> fIdentifierMap;
	protected Map<String, Control> fControlsMap;
	protected ClientConnection fClientConnection;
	protected ClientGUI fClientGUI;
	protected Set<String> fValidIdentifiers;

	public AbstractPageMediator(ClientGUI clientGUI, ClientConnection clientConnection) {
		this.fClientGUI = clientGUI;
		this.fClientConnection = clientConnection;
		this.fIdentifierMap = new HashMap<>();
		this.fControlsMap = new HashMap<>();
		fValidIdentifiers = initializeIdentifiers();
	}

	protected abstract Set<String> initializeIdentifiers();
	
	@Override
	public abstract void notifyEvent(Control control, SelectionEvent event);
	
	@Override
	public void registerControl(Control control, String identifier) {
		if (validIdentifier(identifier)) {	
			fIdentifierMap.put(control, identifier);
			fControlsMap.put(identifier, control);
		}
	}
	
	public boolean validIdentifier(String identifier) {
		return fValidIdentifiers.contains(identifier);
	}
	
	protected String getIdentifier(Control control) {
		return fIdentifierMap.get(control);
	}
	
	protected Control getControl(String identifier) {
		return fControlsMap.get(identifier);
	}
}
