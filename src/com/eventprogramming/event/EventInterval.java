package com.eventprogramming.event;

import java.util.Date;

public class EventInterval {

	private Date fDate;
	private int fStartHour;
	private int fEndHour;
	private int id;
	
	public EventInterval(Date date, int startHour, int endHour, int id) {
		fDate = date;
		fStartHour = startHour;
		fEndHour = endHour;
		this.id = id;
	}

	public Date getDate() {
		return fDate;
	}

	public int getStartHour() {
		return fStartHour;
	}

	public int getEndHour() {
		return fEndHour;
	}
	
	public int getId() {
		return id;
	}
}
