package com.eventprogramming.server.requests;

import org.json.simple.JSONObject;

import com.eventprogramming.constants.Constants;

public class GetIntervalsRequestHandler extends AbstractRequestHandler {

	@Override
	public String handleRequest(JSONObject payload) {
		try {
			String eventCode = (String) payload.get(Constants.EVENT_CODE_KEYWORD);
			String username = null;
			if (payload.containsKey(Constants.USER_KEYWORD))
				username = (String) payload.get(Constants.USER_KEYWORD);

			return fSQLAccess.getEventIntervals(eventCode, username).toString();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}

		return "ERROR";
	}

}
