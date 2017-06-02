package com.eventprogramming.gui.logic;

import org.json.simple.JSONObject;

import com.eventprogramming.constants.Constants;
import com.eventprogramming.server.requests.AbstractRequestHandler;

public class DeleteIntervalsRequestHandler extends AbstractRequestHandler {

	@Override
	public String handleRequest(JSONObject payload) {
		try {
			String eventCode = (String) payload.get(Constants.EVENT_CODE_KEYWORD);

			return fSQLAccess.deleteIntervals(eventCode);
		} catch (ClassCastException e) {
			e.printStackTrace();
		}

		return Constants.SERVER_ERROR;
	}

}
