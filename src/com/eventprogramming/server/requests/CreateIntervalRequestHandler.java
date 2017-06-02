package com.eventprogramming.server.requests;

import org.json.simple.JSONObject;

import com.eventprogramming.constants.Constants;

public class CreateIntervalRequestHandler extends AbstractRequestHandler {

	@Override
	public String handleRequest(JSONObject payload) {
		try {
			String eventCode = (String) payload.get(Constants.EVENT_CODE_KEYWORD);
			String date = (String) payload.get(Constants.DATE_KEYWORD);
			int startHour = ((Long) payload.get(Constants.START_HOUR_KEYWORD)).intValue();
			int endHour = ((Long) payload.get(Constants.END_HOUR_KEYWORD)).intValue();

			return fSQLAccess.insertEventInterval(eventCode, date, startHour, endHour);
		} catch (ClassCastException e) {
			e.printStackTrace();
		}

		return "ERROR";
	}

}
