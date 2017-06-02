package com.eventprogramming.server.requests;

import org.json.simple.JSONObject;

import com.eventprogramming.constants.Constants;

public class CreateEventRequestHandler extends AbstractRequestHandler {

	@Override
	public String handleRequest(JSONObject payload) {
		try {
			String eventName = (String) payload.get(Constants.EVENT_NAME_KEYWORD);
			int isGreedy = ((Long) payload.get(Constants.GREEDY_KEYWORD)).intValue();
			String startDate = (String) payload.get(Constants.MIN_START_DATE_KEYWORD);
			String endDate = (String) payload.get(Constants.MAX_END_DATE_KEYWORD);
			int startHour = ((Long) payload.get(Constants.START_HOUR_KEYWORD)).intValue();
			int endHour = ((Long) payload.get(Constants.END_HOUR_KEYWORD)).intValue();
			int duration = ((Long) payload.get(Constants.DURATION_KEYWORD)).intValue();
			String username = (String) payload.get(Constants.USER_KEYWORD);
			String eventCode = String.valueOf(Math.abs(payload.toJSONString().hashCode()));

			return fSQLAccess.insertEvent(eventName, isGreedy, startDate, endDate, startHour, endHour, duration, username, eventCode);
		} catch (ClassCastException e) {
			e.printStackTrace();
		}

		return "ERROR";
	}

}
