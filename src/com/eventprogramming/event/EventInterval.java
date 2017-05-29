package com.eventprogramming.event;

import java.util.Date;

import com.eventprogramming.event.IntervalVote.VoteType;

public class EventInterval {
	
	private IntervalVote.VoteType vote;
	private Date fDate;
	private int fStartHour;
	private int fEndHour;
	private int id;
	
	public EventInterval(Date date, int startHour, int endHour, int id) {
		fDate = date;
		fStartHour = startHour;
		fEndHour = endHour;
		this.id = id;
		vote = null;
	}

	public void vote(IntervalVote.VoteType voteType) {
		vote = voteType;
	}
	
	public IntervalVote.VoteType getVote() {
		return vote;
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
