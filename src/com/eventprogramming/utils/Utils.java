package com.eventprogramming.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;

import com.eventprogramming.client.ClientGUI.STATE;

public class Utils {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static String getDateString(DateTime dateTime) {
		Date tmp = getDateFromDateTime(dateTime);
		return sdf.format(tmp);
	}
	
	public static Date getDateFromDateTime(DateTime dateTime) {
		return new GregorianCalendar(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay()).getTime();
	}
	
	public static String getDateString(java.sql.Date sqlDate) {
		Date date = new GregorianCalendar(sqlDate.getYear(), sqlDate.getMonth(), sqlDate.getDay()).getTime();
		return sdf.format(date);
	}
	
	public static Date getDateFromString(String dateString) {
		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean openDialog(Shell shell, String title, String message, Runnable okAction, Runnable cancelAction) {
		boolean result = MessageDialog.openConfirm(shell, title, message);
		if (result)
			okAction.run();
		else
			cancelAction.run();
		
		return result;
	}
	

}
