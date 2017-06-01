package com.eventprogramming.event;

public class Priority {

	private int priorityValue;
	private String username;
	
	public Priority(int value, String user) {
		priorityValue = value;
		username = user;
	}

	public int getPriorityValue() {
		return priorityValue;
	}

	public String getUsername() {
		return username;
	}
	
}
