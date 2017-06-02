package com.eventprogramming.server.requests;

import org.json.simple.JSONObject;

public interface RequestHandler {

	String handleRequest(JSONObject payload);
	
}
