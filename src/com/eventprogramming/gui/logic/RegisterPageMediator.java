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

public class RegisterPageMediator extends AbstractPageMediator {

	private static String[] identifiers = { "buttonRegister", "emailText", "userText", "passText" };

	public RegisterPageMediator(ClientGUI clientGUI, ClientConnection clientConnection) {
		super(clientGUI, clientConnection);
	}

	@Override
	public void notifyEvent(Control control, SelectionEvent event) {
		String identifier = getIdentifier(control);
		if ("buttonRegister".equals(identifier)) {
			register(control, event);
		}
	}

	private void register(Control control, SelectionEvent event) {
		if (checkInput()) {
			Text userText = (Text) getControl("userText");
			Text passText = (Text) getControl("passText");
			Text emailText = (Text) getControl("emailText");
			
			boolean registerDone = fClientConnection.sendNewUserCredentials(userText.getText(),
					passText.getText(), emailText.getText());
			if (registerDone)
				fClientGUI.switchState(STATE.WELCOME);
		} else
			Utils.openDialog(fClientGUI.getShell(), "Error",
					"There was an error validating your input. Please double check you introduced a valid email address!",
					() -> {},
					() -> {});
	}
	
	private boolean checkInput() {
		
		Text userText = (Text) getControl("userText");
		Text passText = (Text) getControl("passText");
		Text emailText = (Text) getControl("emailText");

		if (userText.getText().isEmpty())
			return false;
	
		if (passText.getText().isEmpty())
			return false;
	
		if (emailText.getText().isEmpty())
			return false;
		
		return true;
	}
	
	@Override
	protected Set<String> initializeIdentifiers() {
		return new HashSet<>(Arrays.asList(identifiers));
	}

}
