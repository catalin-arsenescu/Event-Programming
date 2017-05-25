package com.eventprogramming.gui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.eventprogramming.gui.logic.DefaultSelectionListener;
import com.eventprogramming.gui.logic.PageMediator;

public class WelcomePageComposite extends Composite {

	private PageMediator fMediator;
	
	public WelcomePageComposite(Composite parent, int style, PageMediator mediator) {
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
		userText.setText("admin");
		userText.setTextLimit(100);
		userText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fMediator.registerControl(userText, "userText");

		Label passLabel = new Label(mainGroup, SWT.NONE);
		passLabel.setText("Password:");
		passLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));

		Text passText = new Text(mainGroup, SWT.SINGLE | SWT.BORDER);
		passText.setText("admin");
		passText.setTextLimit(100);
		passText.setEchoChar('*');
		passText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fMediator.registerControl(passText, "passText");

		Button buttonRegister = new Button(mainGroup, SWT.PUSH);
		buttonRegister.setText("Register");
		buttonRegister.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		fMediator.registerControl(buttonRegister, "buttonRegister");
		buttonRegister.addSelectionListener(new DefaultSelectionListener(fMediator, buttonRegister));

		Button buttonLogin = new Button(mainGroup, SWT.PUSH);
		buttonLogin.setText("Login");
		buttonLogin.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		fMediator.registerControl(buttonLogin, "buttonLogin");
		buttonLogin.addSelectionListener(new DefaultSelectionListener(fMediator, buttonLogin));
	}
}
