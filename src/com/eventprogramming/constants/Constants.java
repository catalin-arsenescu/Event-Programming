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
	
	public static final String		EVENT_KEYWORD				= "event";
	
	// Service names
	public static final String		CREATE_USER_SERVICE				= "create-user";
	public static final String		LOGIN_SERVICE					= "login";
	public static final String 		CREATE_EVENT_SERVICE 			= "create-event";
	public static final String 		CREATE_EVENT_INTERVAL_SERVICE 	= "create-event-interval";
	public static final String 		GENERATE_INTERVALS_SERVICE 		= "generate-event-intervals";

	public static final String 		SERVER_OFFLINE_ERROR 	= "Server cannot be reached! Check your Internet connection and try again!";






}
