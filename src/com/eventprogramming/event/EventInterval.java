package com.eventprogramming.event;

import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.eventprogramming.constants.Constants;
import com.eventprogramming.event.IntervalVote.VoteType;
import com.eventprogramming.utils.Utils;

public class EventInterval {
	
	private IntervalVote.VoteType vote;
	private Date fDate;
	private int fStartHour;
	private int fEndHour;
	private int id;
	
	/**
	 * All votes for this interval
	 */
	private ArrayList<IntervalVote> fVotes;
	
	public EventInterval(Date date, int startHour, int endHour, int id) {
		fDate = date;
		fStartHour = startHour;
		fEndHour = endHour;
		this.id = id;
		vote = null;
	}

	public void vote(IntervalVote.VoteType voteType) {
		vote = voteType;
	}
	
	public IntervalVote.VoteType getVote() {
		return vote;
	}
	
	public Date getDate() {
		return fDate;
	}

	public int getStartHour() {
		return fStartHour;
	}

	public int getEndHour() {
		return fEndHour;
	}
	
	public int getId() {
		return id;
	}

	public void addVotes(String voteString) {
		fVotes = new ArrayList<>();
		JSONObject json;
		JSONObject vote;
		try {
			json = (JSONObject) new JSONParser().parse(voteString);
			int i = 1;
			while ((vote = (JSONObject) json.get(Constants.VOTE_KEYWORD + i++)) != null) {
				int intervalId 		= ((Long) vote.get(Constants.INTERVAL_ID_KEYWORD)).intValue();
				int voteId			= ((Long) vote.get(Constants.VOTE_ID_KEYWORD)).intValue();
				String username		= (String) vote.get(Constants.USER_KEYWORD);
				int voteType		= ((Long) vote.get(Constants.VOTE_TYPE_KEYWORD)).intValue();
				
				fVotes.add(new IntervalVote(voteId, intervalId, VoteType.getFromInt(voteType), username));
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getVotes(VoteType voteType) {
		if (fVotes == null)
			return 0;
	
		int count = 0;
		for (IntervalVote vote : fVotes)
			if (vote.getType() == voteType)
				count++;
			
		return count;
	}
}
