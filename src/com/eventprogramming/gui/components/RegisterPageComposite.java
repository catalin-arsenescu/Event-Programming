package com.eventprogramming.gui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.eventprogramming.client.ClientGUI.STATE;
import com.eventprogramming.gui.logic.DefaultSelectionListener;
import com.eventprogramming.gui.logic.PageMediator;

public class RegisterPageComposite extends Composite {

	private PageMediator fMediator;
	
	public RegisterPageComposite(Composite parent, int style, PageMediator mediator) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		fMediator = mediator;
		buildComposite();
	}

	private void buildComposite() {
		Group mainGroup = new Group(this, SWT.NONE);
		mainGroup.setLayout(new GridLayout(2, true));
		mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

		Label userLabel = new Label(mainGroup, SWT.NONE);
		userLabel.setText("User name:");
		userLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Text userText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		userText.setText("");
		userText.setTextLimit(100);
		userText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fMediator.registerControl(userText, "userText");

		Label passLabel = new Label(mainGroup, SWT.NONE);
		passLabel.setText("Password:");
		passLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Text passText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		passText.setText("");
		passText.setTextLimit(100);
		passText.setEchoChar('*');
		passText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fMediator.registerControl(passText, "passText");

		Label emailLabel = new Label(mainGroup, SWT.NONE);
		emailLabel.setText("Email:");
		emailLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		Text emailText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		emailText.setText("");
		emailText.setTextLimit(100);
		emailText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fMediator.registerControl(emailText, "emailText");

		Button buttonRegister = new Button(this, SWT.PUSH);
		buttonRegister.setText("Register");
		buttonRegister.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		fMediator.registerControl(buttonRegister, "buttonRegister");
		buttonRegister.addSelectionListener(new DefaultSelectionListener(fMediator, buttonRegister));
	}
}