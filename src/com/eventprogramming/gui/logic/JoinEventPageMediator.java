package com.eventprogramming.gui.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.eventprogramming.client.ClientConnection;
import com.eventprogramming.client.ClientGUI;
import com.eventprogramming.client.ClientGUI.STATE;
import com.eventprogramming.event.Event;
import com.eventprogramming.event.EventInterval;
import com.eventprogramming.event.IntervalVote;
import com.eventprogramming.event.IntervalVote.VoteType;
import com.eventprogramming.utils.Utils;

public class JoinEventPageMediator extends AbstractPageMediator {

	private static String[] identifiers = { "tabFolder", "saveButton" };
	private Event fEvent;

	public JoinEventPageMediator(ClientGUI clientGUI, ClientConnection clientConnection) {
		super(clientGUI, clientConnection);
	}

	@Override
	public void notifyEvent(Control control, SelectionEvent event) {
		String identifier = getIdentifier(control);
		if ("saveButton".equals(identifier)) {
			sendVotes();
		} 
	}

	private void sendVotes() {
		if (fEvent == null)
			return;
		
		List<EventInterval> intervals = fEvent.getIntervals();
		List<IntervalVote> submittedVotes = new ArrayList<>();
		for (EventInterval interval : intervals) {
			if (interval.getVote() == null)
				continue;		// No vote submitted
			
			IntervalVote vote = new IntervalVote(-1, interval.getId(), interval.getVote(), fClientGUI.getUsername());
			submittedVotes.add(vote);
		} 
		
		if (!submittedVotes.isEmpty())
			fClientConnection.sendEventVotes(submittedVotes);
	}
	
	public void setEvent(Event event) {
		this.fEvent = event;
	}
	
	@Override
	protected Set<String> initializeIdentifiers() {
		return new HashSet<>(Arrays.asList(identifiers));
	}

}
