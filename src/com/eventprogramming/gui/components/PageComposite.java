package com.eventprogramming.gui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.eventprogramming.gui.logic.PageMediator;
import com.eventprogramming.widgets.BackButton;

public abstract class PageComposite extends Composite {

	protected PageMediator fMediator;
	
	public PageComposite(Composite parent, int style, PageMediator pageMediator) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		fMediator = pageMediator;
		installBackButton();
	}

	protected abstract void buildComposite();

	private void installBackButton() {
		BackButton button = new BackButton(this, SWT.ARROW | SWT.LEFT);
		button.setBounds(0, 0, 50, 50);
	}

	@Override
	protected void checkSubclass() {}
}
