package com.eventprogramming.event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.eventprogramming.constants.Constants;
import com.eventprogramming.utils.Utils;

public class EventCache {

	private String username;
	private List<Event> eventCache;
	
	public EventCache() {
		eventCache = new ArrayList<Event>();
	}
	
	public void addEvent(Event e) {
		eventCache.add(e);
	}
	
	public String[] toNameArray() {
		List<String> collect = eventCache.stream().
				map(e -> e.getName()).
				collect(Collectors.toList());
		
		return collect.toArray(new String[0]);
				
	}

	public void addEvents(String jsonString) {
		JSONObject json;
		JSONObject event;
		try {
			json = (JSONObject) new JSONParser().parse(jsonString);
			int i = 1;
			while ((event = (JSONObject) json.get(Constants.EVENT_KEYWORD + i++)) != null) {
				String eventName	= (String) event.get(Constants.EVENT_NAME_KEYWORD);
				int isGreedy 		= ((Long) event.get(Constants.GREEDY_KEYWORD)).intValue();
				String startDate	= (String) event.get(Constants.MIN_START_DATE_KEYWORD);
				String endDate		= (String) event.get(Constants.MAX_END_DATE_KEYWORD);
				int startHour		= ((Long) event.get(Constants.START_HOUR_KEYWORD)).intValue();
				int endHour	 		= ((Long) event.get(Constants.END_HOUR_KEYWORD)).intValue();
				int duration     	= ((Long) event.get(Constants.DURATION_KEYWORD)).intValue();
				String eventCode	= (String) event.get(Constants.EVENT_CODE_KEYWORD);
				
				addEvent(new Event(eventName, username, isGreedy, Utils.getDateFromString(startDate),
								Utils.getDateFromString(endDate), startHour, endHour, duration, eventCode));
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setUsername(String sessionUsername) {
		this.username = sessionUsername;
	}
}
