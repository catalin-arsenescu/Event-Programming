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

public class WelcomePageMediator extends AbstractPageMediator {

	private static String[] identifiers = { "buttonRegister", "buttonLogin", "userText", "passText" };

	public WelcomePageMediator(ClientGUI clientGUI, ClientConnection clientConnection) {
		super(clientGUI, clientConnection);
	}

	@Override
	public void notifyEvent(Control control, SelectionEvent event) {
		String identifier = getIdentifier(control);
		if ("buttonRegister".equals(identifier)) {
			register(control, event);
		} else if ("buttonLogin".equals(identifier)) {
			login(control, event);
		}
	}

	private void register(Control control, SelectionEvent event) {
		fClientGUI.switchState(STATE.REGISTER);
	}

	private void login(Control control, SelectionEvent event) {

		if (checkInput()) {
			Text userText = (Text) getControl("userText");
			Text passText = (Text) getControl("passText");
			boolean loginOK = fClientConnection.sendLogin(userText.getText(), passText.getText());
			if (loginOK) {
				fClientGUI.sessionUsername = userText.getText();
				fClientGUI.fEventCache.setUsername(fClientGUI.sessionUsername);
				fClientGUI.switchState(STATE.EVENT);
			} // else openDialog(/* Error message */);
		} else {
			//openDialog(/* Error message */);
		}
	}
	
	private boolean checkInput() {
		
		Text userText = (Text) getControl("userText");
		Text passText = (Text) getControl("passText");
		
		if (userText.getText().isEmpty())
			return false;

		if (passText.getText().isEmpty())
			return false;

		return true;
	}
	
	@Override
	protected Set<String> initializeIdentifiers() {
		return new HashSet<>(Arrays.asList(identifiers));
	}

}
