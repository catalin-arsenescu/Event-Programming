package com.eventprogramming.event;

import java.util.Date;

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
}
