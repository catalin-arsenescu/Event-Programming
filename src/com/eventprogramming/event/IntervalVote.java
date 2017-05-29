package com.eventprogramming.event;

public class IntervalVote {

	public enum VoteType {
		YES, NO, IFB
	}
	
	private int intervalID;
	private int voteID;
	private VoteType type;
	private String username;
	
	public IntervalVote(int voteID, int intervalID, VoteType type, String username) {
		this.intervalID = intervalID;
		this.voteID = voteID;
		this.type = type;
		this.username = username;
	}

	public int getIntervalID() {
		return intervalID;
	}

	public int getVoteID() {
		return voteID;
	}

	public VoteType getType() {
		return type;
	}
	
	public int getTypeInt() {
		return type == VoteType.YES ? 0 :
				type == VoteType.NO ? 1 : 2;
											
	}

	public String getUsername() {
		return username;
	}
	
}
