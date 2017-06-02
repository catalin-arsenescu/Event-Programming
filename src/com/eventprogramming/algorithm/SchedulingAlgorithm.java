package com.eventprogramming.algorithm;

import java.util.List;
import java.util.Map;

import com.eventprogramming.event.EventInterval;
import com.eventprogramming.event.IntervalVote;
import com.eventprogramming.event.IntervalVote.VoteType;
import com.eventprogramming.event.Priority;

public class SchedulingAlgorithm {

	private static final int LOW_PRIORITY = Integer.MIN_VALUE;
	
	/** TODO: rename
	 * 
	 * @return
	 */
	public static int computeFeasibility(EventInterval interval) {
		if (interval == null)
			return LOW_PRIORITY;
		
		Map<String, Integer> priorityMap = interval.getPriorityMap();
		List<IntervalVote> votes = interval.getVotes();
		
		int feasibility = 0;
		
		for (IntervalVote vote : votes) {
			int weight = getTypeWeight(vote.getType());
			int priority = getPriorityWithMap(priorityMap, vote.getUsername());
			
			feasibility += weight * priority;
		}
		
		return feasibility;
	}
	
	private static int getPriorityWithMap(Map<String, Integer> priorityMap, String username) {
		boolean hasPriority = priorityMap.containsKey(username);
		return hasPriority ? priorityMap.get(username) : 1;
	}

	private static int getTypeWeight(VoteType voteType) {
		if (voteType == VoteType.YES) 
			return 1;
		else if (voteType == VoteType.NO)
			return -1;
		else
			return 0;
	}
}
