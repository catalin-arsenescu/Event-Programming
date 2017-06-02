package com.eventprogramming.server.requests;

import org.json.simple.JSONObject;

import com.eventprogramming.constants.Constants;

public class SaveVotesRequestHandler extends AbstractRequestHandler {

	@Override
	public String handleRequest(JSONObject payload) {
		JSONObject interval;
		try {
			int i = 1;
			while ((interval = (JSONObject) payload.get(Constants.VOTE_KEYWORD + i++)) != null) {
				int intervalId = ((Long) interval.get(Constants.INTERVAL_ID_KEYWORD)).intValue();
				String username = (String) interval.get(Constants.USER_KEYWORD);
				int voteType = ((Long) interval.get(Constants.VOTE_TYPE_KEYWORD)).intValue();
				int eventId = fSQLAccess.getEventIdFromIntervalId(intervalId);

				fSQLAccess.addVote(eventId, intervalId, username, voteType);
			}

			return Constants.SERVER_OK;

		} catch (ClassCastException e) {
			e.printStackTrace();
		}

		return Constants.SERVER_ERROR;
	}
}
