package com.eventprogramming.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.eventprogramming.constants.Constants;
import com.eventprogramming.utils.Utils;

public class Event {

	private String name;
	private String username;
	private int type;
	private Date minStartDate;
	private Date maxEndDate;
	private int startHour;
	private int endHour;
	private int duration;
	private String eventCode;
	private List<EventInterval> fEventIntervals;

	public Event(String name, String username, int type, Date minStartDate,
				Date maxEndDate, int startHour, int endHour, int duration, String eventCode) {
		this.name = name;
		this.username = username;
		this.type = type;
		this.minStartDate = minStartDate;
		this.maxEndDate = maxEndDate;
		this.startHour = startHour;
		this.endHour = endHour;
		this.duration = duration;
		this.eventCode = eventCode;
	}

	public String getEventCode() {
		return eventCode;
	}
	
	public String getName() {
		return name;
	}

	public String getUsername() {
		return username;
	}

	public int getType() {
		return type;
	}

	public Date getMinStartDate() {
		return minStartDate;
	}

	public Date getMaxEndDate() {
		return maxEndDate;
	}

	public int getStartHour() {
		return startHour;
	}

	public int getEndHour() {
		return endHour;
	}

	public int getDuration() {
		return duration;
	}

	public void setIntervals(List<EventInterval> intervals) {
		fEventIntervals = new ArrayList<>(intervals);
	}
	
	public List<EventInterval> getIntervals() {
		return fEventIntervals;
	}

	public void parseAndSetIntervals(String response) {
		fEventIntervals = new ArrayList<>();
		JSONObject json;
		JSONObject interval;
		try {
			json = (JSONObject) new JSONParser().parse(response);
			int i = 1;
			while ((interval = (JSONObject) json.get(Constants.INTERVAL_KEYWORD + i++)) != null) {
				int intervalId 		= ((Long) interval.get(Constants.INTERVAL_ID_KEYWORD)).intValue();
				String startDate	= (String) interval.get(Constants.DATE_KEYWORD);
				int startHour		= ((Long) interval.get(Constants.START_HOUR_KEYWORD)).intValue();
				int endHour	 		= ((Long) interval.get(Constants.END_HOUR_KEYWORD)).intValue();
				
				fEventIntervals.add(new EventInterval(Utils.getDateFromString(startDate), startHour, endHour, intervalId));
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
