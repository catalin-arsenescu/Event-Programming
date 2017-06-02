package com.eventprogramming.server.requests;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.eventprogramming.constants.Constants;
import com.eventprogramming.database.MySQLAccess;
import com.eventprogramming.gui.logic.DeleteIntervalsRequestHandler;

public abstract class AbstractRequestHandler implements RequestHandler {

	protected final MySQLAccess fSQLAccess;
	
	public AbstractRequestHandler() {
		fSQLAccess = new MySQLAccess();
	}
	
	@Override
	public abstract String handleRequest(JSONObject payload);

	public static Map<String, RequestHandler> getAllHandlers() {
		Map<String, RequestHandler> handlers = new HashMap<>();
		handlers.put(Constants.MESSAGE_TYPE_LOGIN, new LoginRequestHandler());
		handlers.put(Constants.MESSAGE_TYPE_REGISTER, new RegisterRequestHandler());
		handlers.put(Constants.MESSAGE_TYPE_CREATE_EVENT, new CreateEventRequestHandler());
		handlers.put(Constants.MESSAGE_TYPE_CREATE_INTERVAL, new CreateIntervalRequestHandler());
		handlers.put(Constants.MESSAGE_TYPE_GET_INTERVALS, new GetIntervalsRequestHandler());
		handlers.put(Constants.MESSAGE_TYPE_GET_EVENTS, new GetEventsRequestHandler());
		handlers.put(Constants.MESSAGE_TYPE_SAVE_VOTES, new SaveVotesRequestHandler());
		handlers.put(Constants.MESSAGE_TYPE_GET_VOTES, new GetVotesRequestHandler());
		handlers.put(Constants.MESSAGE_TYPE_ADD_PRIORITY, new AddPriorityRequestHandler());
		handlers.put(Constants.MESSAGE_TYPE_GET_PRIORITIES, new GetPrioritiesRequestHandler());
		handlers.put(Constants.MESSAGE_TYPE_DELETE_INTEVALS, new DeleteIntervalsRequestHandler());
		return handlers;
	}
}
