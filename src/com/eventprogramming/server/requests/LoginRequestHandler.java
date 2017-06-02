package com.eventprogramming.server.requests;

import org.json.simple.JSONObject;

import com.eventprogramming.constants.Constants;

public class LoginRequestHandler extends AbstractRequestHandler {

	@Override
	public String handleRequest(JSONObject payload) {
		try {
			String username = (String) payload.get(Constants.USER_KEYWORD);
			String password = (String) payload.get(Constants.PASS_KEYWORD);

			return fSQLAccess.login(username, password);
		} catch (ClassCastException e) {
			e.printStackTrace();
		}

		return Constants.SERVER_ERROR;
	}

}
