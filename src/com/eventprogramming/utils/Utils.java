package com.eventprogramming.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.eventprogramming.constants.Constants;
import com.eventprogramming.event.Event;
import com.eventprogramming.event.EventInterval;
import com.eventprogramming.event.Priority;

public class Utils {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static String printDate(Date date) {
		return sdf.format(date);
	}
	
	public static String getDateString(DateTime dateTime) {
		Date tmp = getDateFromDateTime(dateTime);
		return sdf.format(tmp);
	}

	public static Date getDateFromDateTime(DateTime dateTime) {
		return new GregorianCalendar(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay()).getTime();
	}

	public static String getDateString(java.sql.Date sqlDate) {
		java.util.Date newDate = new java.util.Date(sqlDate.getTime());
		LocalDate start = newDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return start.toString();
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

	public static boolean openDialog(Shell shell, String title, String message, Runnable okAction,
			Runnable cancelAction) {
		boolean result = MessageDialog.openConfirm(shell, title, message);
		if (result)
			okAction.run();
		else
			cancelAction.run();

		return result;
	}

	public static boolean checkDateWithDateInterval(Date minStartDate, Date tmp, Date maxEndDate) {
		return true;
	}
	
	public static List<Priority> parsePriorities(String clientSentence) {
		ArrayList<Priority> priorities = new ArrayList<>();
		JSONObject json;
		JSONObject priorityJSON;
		try {
			json = (JSONObject) new JSONParser().parse(clientSentence);
			int i = 1;
			while ((priorityJSON = (JSONObject) json.get(Constants.PRIORITY_KEYWORD + i++)) != null) {
				int priorityValue	= ((Long) priorityJSON.get(Constants.PRIORITY_VALUE_KEYWORD)).intValue();
				String username 	= (String) priorityJSON.get(Constants.USER_KEYWORD);
				
				Priority priority = new Priority(priorityValue, username);
				priorities.add(priority);
			}
		} catch (ClassCastException | org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		
		return priorities;
	}

	public static List<EventInterval> parseIntervals(Event event, String response) {
		ArrayList<EventInterval> intervals = new ArrayList<>();
		JSONObject json;
		JSONObject intervalJSON;
		try {
			json = (JSONObject) new JSONParser().parse(response);
			int i = 1;
			while ((intervalJSON = (JSONObject) json.get(Constants.INTERVAL_KEYWORD + i++)) != null) {
				int intervalId 		= ((Long) intervalJSON.get(Constants.INTERVAL_ID_KEYWORD)).intValue();
				String startDate	= (String) intervalJSON.get(Constants.DATE_KEYWORD);
				int startHour		= ((Long) intervalJSON.get(Constants.START_HOUR_KEYWORD)).intValue();
				int endHour	 		= ((Long) intervalJSON.get(Constants.END_HOUR_KEYWORD)).intValue();
				int existingVote    = ((Long) intervalJSON.get(Constants.VOTE_KEYWORD)).intValue();
				
				EventInterval interval = new EventInterval(event, Utils.getDateFromString(startDate), startHour, endHour, intervalId, existingVote);
				intervals.add(interval);
			}
			
		} catch (ClassCastException | org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		
		return intervals;
	}

}
