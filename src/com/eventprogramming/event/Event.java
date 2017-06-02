package com.eventprogramming.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private List<Priority> fPriorities;
	private Map<String, Integer> fPriorityMap;

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
		fPriorities = new ArrayList<>();
		fPriorityMap = new HashMap<>();
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
		Collections.sort(fEventIntervals);
		return fEventIntervals;
	}
	
	public List<Priority> getPriorities() {
		return fPriorities;
	}
	
	public Map<String, Integer> getPriorityMap() {
		return fPriorityMap;
	}
	
	public void setPriorities(List<Priority> priorities) {
		fPriorities = new ArrayList<>();
		fPriorityMap = new HashMap<>();
		
		for (Priority priority : priorities) {
			fPriorities.add(priority);
			fPriorityMap.put(priority.getUsername(), priority.getPriorityValue());
		}
	}

	public void addVotes(String response) {
		JSONObject json;
		JSONObject intervalJSON;
		try {
			json = (JSONObject) new JSONParser().parse(response);
			int i = 1;
			while ((intervalJSON = (JSONObject) json.get(Constants.INTERVAL_KEYWORD + i++)) != null) {
				if (intervalJSON.get(Constants.VOTE_KEYWORD + '1') == null) // No votes
					continue;
				
				int intervalId 		= ((Long) ((JSONObject) intervalJSON.get(Constants.VOTE_KEYWORD + '1')).get(Constants.INTERVAL_ID_KEYWORD)).intValue();
				EventInterval interval = getIntervalById(intervalId);
				if (interval == null)
					continue;
				
				String voteString = intervalJSON.toJSONString();
				interval.addVotes(voteString);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private EventInterval getIntervalById(int id) {
		for (EventInterval interval : fEventIntervals)
			if (interval.getId() == id)
				return interval;
						
		return null;
	}
	
	public String toString() {
		return "Event " + name + " initiated by " + username + " code " + eventCode; 
	}

}
