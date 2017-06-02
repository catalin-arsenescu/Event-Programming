package com.eventprogramming.server.requests;

import org.json.simple.JSONObject;

import com.eventprogramming.constants.Constants;

public class AddPriorityRequestHandler extends AbstractRequestHandler {

	@Override
	public String handleRequest(JSONObject payload) {
		try {
			String eventCode = (String) payload.get(Constants.EVENT_CODE_KEYWORD);
			String username = (String) payload.get(Constants.USER_KEYWORD);
			int priority = ((Long) payload.get(Constants.PRIORITY_VALUE_KEYWORD)).intValue();

			return fSQLAccess.addPriority(eventCode, username, priority);
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		
		return Constants.SERVER_ERROR;
	}

}
