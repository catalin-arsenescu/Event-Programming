package com.eventprogramming.constants;

/**
 * Defines parameters used throughout the application
 * @author Catalin
 *
 */
public class Constants {
	
	// Version
	public static final String 		APP_VERSION 			= "v1.0";
	
	// Titles and labels
	public static final String		APP_WINDOW_TITLE 		= "Event Programming";
	
	// Separators
	public static final String 		SEP_SPACE 				= " ";

	// MySQL Constants
	public static final String 		DB_SCHEMA_ADDRESS 		= "jdbc:mysql://localhost/feedback?";
	public static final String		DB_CREDENTIALS			= "user=sqluser&password=sqluserpw";
	
	// JSON Communication Keywords
	public static final String		USER_KEYWORD			= "username";
	public static final String		PASS_KEYWORD			= "password";
	public static final String		EMAIL_KEYWORD			= "email";
	public static final String		SALT_KEYWORD			= "salt";

	public static final String		EVENT_NAME_KEYWORD			= "event_name";
	public static final String		GREEDY_KEYWORD				= "is_greedy";
	public static final String		MIN_START_DATE_KEYWORD		= "min_start_date";
	public static final String		MAX_END_DATE_KEYWORD		= "max_end_date";
	public static final String		START_HOUR_KEYWORD			= "start_hour";
	public static final String		END_HOUR_KEYWORD			= "end_hour";
	public static final String		DURATION_KEYWORD			= "duration";
	public static final String		EVENT_CODE_KEYWORD			= "event_code";
	public static final String 		DATE_KEYWORD 				= "date";
	public static final String 		INTERVAL_ID_KEYWORD 		= "interval-id";
	public static final String 		VOTE_ID_KEYWORD 			= "vote-id";
	public static final String 		PRIORITY_VALUE_KEYWORD 		= "priority-value";
	public static final String 		VOTE_TYPE_KEYWORD 			= "vote-type";
	
	public static final String		EVENT_KEYWORD				= "event";
	public static final String		VOTE_KEYWORD				= "vote";
	public static final String		INTERVAL_KEYWORD			= "interval";
	public static final String		PRIORITY_KEYWORD			= "priority";
	
	// Service names
	public static final String		CREATE_USER_SERVICE				= "create-user";
	public static final String		LOGIN_SERVICE					= "login";
	public static final String 		CREATE_EVENT_SERVICE 			= "create-event";
	public static final String 		GET_EVENTS_SERVICE 				= "get-events";
	public static final String 		ADD_PRIORITY_SERVICE			= "add-priority";
	public static final String 		CREATE_EVENT_INTERVAL_SERVICE 	= "create-event-interval";
	public static final String 		GENERATE_INTERVALS_SERVICE 		= "generate-event-intervals";
	public static final String 		EVENT_INTERVALS_SERVICE 		= "event-intervals";
	public static final String		SEND_VOTES_SERVICE				= "send-votes";

	public static final String 		SERVER_OFFLINE_ERROR 	= "Server cannot be reached! Check your Internet connection and try again!";
	
	// Server port
	public static final int 		MAIN_SERVER_PORT				= 6888;

	// Server error messages
	public static final String 		SERVER_ERROR 					= "ERROR";
	public static final String 		SERVER_OK 					= "OK";
	
	// Message types
	public static final String		MESSAGE_TYPE					= "MESSAGE-TYPE";
	public static final String		MESSAGE_PAYLOAD					= "MESSAGE-PAYLOAD";
	public static final String		MESSAGE_TYPE_LOGIN				= "MESSAGE-TYPE-LOGIN";
	public static final String		MESSAGE_TYPE_REGISTER			= "MESSAGE-TYPE-REGISTER";
	public static final String		MESSAGE_TYPE_CREATE_EVENT		= "MESSAGE-TYPE-CREATE-EVENT";
	public static final String		MESSAGE_TYPE_CREATE_INTERVAL	= "MESSAGE-TYPE-CREATE-INTERVAL";
	public static final String		MESSAGE_TYPE_GET_INTERVALS		= "MESSAGE-TYPE-GET-INTERVALS";
	public static final String		MESSAGE_TYPE_GET_EVENTS			= "MESSAGE-TYPE-GET-EVENTS";
	public static final String		MESSAGE_TYPE_SAVE_VOTES			= "MESSAGE-TYPE-SAVE-VOTES";
	public static final String		MESSAGE_TYPE_GET_VOTES			= "MESSAGE-TYPE-GET-VOTES";
	public static final String		MESSAGE_TYPE_ADD_PRIORITY		= "MESSAGE-TYPE-ADD-PRIORITY";
	public static final String		MESSAGE_TYPE_GET_PRIORITIES		= "MESSAGE-TYPE-GET-PRIORITES";
	public static final String 		MESSAGE_TYPE_DELETE_INTEVALS 	= "MESSAGE-TYPE-DELETE-INTERVALS";


}
