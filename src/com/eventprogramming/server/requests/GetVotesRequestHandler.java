package com.eventprogramming.server.requests;

import org.json.simple.JSONObject;

import com.eventprogramming.constants.Constants;

public class GetVotesRequestHandler extends AbstractRequestHandler {

	@Override
	public String handleRequest(JSONObject payload) {
		try {
			
			String eventCode = (String) payload.get(Constants.EVENT_CODE_KEYWORD);
			return fSQLAccess.getVotesForEvent(eventCode).toString();
			
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		
		return Constants.SERVER_ERROR;
	}

}
