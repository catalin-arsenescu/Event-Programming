package com.eventprogramming.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import org.eclipse.swt.widgets.DateTime;
import org.json.simple.JSONObject;

import com.eventprogramming.constants.Constants;
import com.eventprogramming.event.Event;
import com.eventprogramming.event.IntervalVote;
import com.eventprogramming.utils.Utils;

public class ClientConnection {

	private ClientGUI fClientGUI;
	private final SecureRandom fRandomGenerator;

	public ClientConnection(ClientGUI clientGUI) {
		this.fClientGUI = clientGUI;
		fRandomGenerator = new SecureRandom(ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(System.nanoTime()).array());
	}
	
	@SuppressWarnings("unchecked")
	private String sendMessage(String messageType, JSONObject payload) {
		JSONObject json = new JSONObject();
		json.put(Constants.MESSAGE_TYPE, messageType);
		json.put(Constants.MESSAGE_PAYLOAD, payload);
		String message = json.toJSONString() + '\n'; 
		System.out.println("SENDING" + message);
		
		try {
			
			Socket clientSocket = new Socket("localhost", Constants.MAIN_SERVER_PORT);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer.writeBytes(message);
			String response = inFromServer.readLine();
			clientSocket.close();
			System.out.println("FROM SERVER: " + response);
			return response == null ? "" : response;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Constants.SERVER_ERROR;
	}
	
	@SuppressWarnings("unchecked")
	public boolean sendLogin(String username, String password) {
		JSONObject payload = new JSONObject();
		payload.put(Constants.USER_KEYWORD, username);
		payload.put(Constants.PASS_KEYWORD, password);
		
		String response = sendMessage(Constants.MESSAGE_TYPE_LOGIN, payload);
		if (Constants.SERVER_ERROR.equals(response)) {
			return false;
		} else {
			fClientGUI.fEventCache.addEvents(response);
			return true;
		}
	}

	@SuppressWarnings({"unchecked"})
	public boolean sendNewUserCredentials(String username, String password, String email) {
		JSONObject payload = new JSONObject();
		payload.put(Constants.USER_KEYWORD, username);
		payload.put(Constants.PASS_KEYWORD, password);
		payload.put(Constants.EMAIL_KEYWORD, email);
		payload.put(Constants.SALT_KEYWORD, generateSalt());
		
		String response = sendMessage(Constants.MESSAGE_TYPE_REGISTER, payload);
		if (Constants.SERVER_OK.equals(response))
			return true;
		else 
			return false;
	}

	@SuppressWarnings("unchecked")
	public String sendNewEvent(String eventName, boolean isGreedy, DateTime startDate, DateTime endDate, int startHour, int endHour, int duration, String username) {
		JSONObject payload = new JSONObject();
		payload.put(Constants.EVENT_NAME_KEYWORD, eventName);
		payload.put(Constants.GREEDY_KEYWORD, isGreedy ? 1 : 0);
		payload.put(Constants.MIN_START_DATE_KEYWORD, Utils.getDateString(startDate));
		payload.put(Constants.MAX_END_DATE_KEYWORD, Utils.getDateString(endDate));
		payload.put(Constants.START_HOUR_KEYWORD, startHour);
		payload.put(Constants.END_HOUR_KEYWORD, endHour);
		payload.put(Constants.DURATION_KEYWORD, duration);
		payload.put(Constants.USER_KEYWORD, username);
		
		String response = sendMessage(Constants.MESSAGE_TYPE_CREATE_EVENT, payload);
		return response;
	}
	
	public String sendNewEventInterval(String eventCode, DateTime date, int startHour, int endHour) {
		return sendNewEventInterval(eventCode, Utils.getDateString(date), startHour, endHour);
	}
	
	@SuppressWarnings("unchecked")
	public String deleteEventIntervals(String eventCode) {
		JSONObject payload = new JSONObject();
		payload.put(Constants.EVENT_CODE_KEYWORD, eventCode);
		
		String response = sendMessage(Constants.MESSAGE_TYPE_DELETE_INTEVALS, payload);
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public String sendNewEventInterval(String eventCode, String date, int startHour, int endHour) {
		JSONObject payload = new JSONObject();
		payload.put(Constants.EVENT_CODE_KEYWORD, eventCode);
		payload.put(Constants.DATE_KEYWORD, date);
		payload.put(Constants.START_HOUR_KEYWORD, startHour);
		payload.put(Constants.END_HOUR_KEYWORD, endHour);
		
		String response = sendMessage(Constants.MESSAGE_TYPE_CREATE_INTERVAL, payload);
		return response;
	}

	@SuppressWarnings("unchecked")
	public void sendEventVotes(List<IntervalVote> votes) {
		JSONObject payload = new JSONObject();
		int i = 1;
		for (IntervalVote interval : votes) {
			JSONObject voteJson = new JSONObject();

			String username = interval.getUsername();
			voteJson.put(Constants.USER_KEYWORD, username);

			int interval_id = interval.getIntervalID();
			voteJson.put(Constants.INTERVAL_ID_KEYWORD, interval_id);

			int vote_id = -1;
			voteJson.put(Constants.VOTE_ID_KEYWORD, vote_id);

			int voteType = interval.getTypeInt();
			voteJson.put(Constants.VOTE_TYPE_KEYWORD, voteType);

			payload.put(Constants.VOTE_KEYWORD + i++, voteJson);
		}
		
		sendMessage(Constants.MESSAGE_TYPE_SAVE_VOTES, payload);
	}

	@SuppressWarnings("unchecked")
	public void getPrioritiesForEvent(Event event) {
		JSONObject payload = new JSONObject();
		payload.put(Constants.EVENT_CODE_KEYWORD, event.getEventCode());
		String response = sendMessage(Constants.MESSAGE_TYPE_GET_PRIORITIES, payload);
		if (!Constants.SERVER_ERROR.equals(response))
			event.setPriorities(Utils.parsePriorities(response));
	}
	
	@SuppressWarnings("unchecked")
	public List<IntervalVote> getVotesForEvent(Event event) {
		JSONObject payload = new JSONObject();
		payload.put(Constants.EVENT_CODE_KEYWORD, event.getEventCode());
		String response = sendMessage(Constants.MESSAGE_TYPE_GET_VOTES, payload);
		event.addVotes(response);
		return null;
	}

	public void getEventIntervals(Event selectedEvent) {
		getEventIntervals(selectedEvent, null);
	}
	
	@SuppressWarnings("unchecked")
	public void getEventIntervals(Event selectedEvent, String username) {
		JSONObject payload = new JSONObject();
		payload.put(Constants.EVENT_CODE_KEYWORD, selectedEvent.getEventCode());
		if (username != null)
			payload.put(Constants.USER_KEYWORD, username);
		String response = sendMessage(Constants.MESSAGE_TYPE_GET_INTERVALS, payload);		
		if (!"ERROR".equals(response))
			selectedEvent.setIntervals(Utils.parseIntervals(selectedEvent, response));
	}

	@SuppressWarnings("unchecked")
	public String getEventForCode(String eventCode) {
		JSONObject payload = new JSONObject();
		payload.put(Constants.EVENT_CODE_KEYWORD, eventCode);
		String response = sendMessage(Constants.MESSAGE_TYPE_GET_EVENTS, payload);
		return response;
	}

	@SuppressWarnings("unchecked")
	public String addPriority(String eventCode, String username, int priority) {
		JSONObject payload = new JSONObject();
		payload.put(Constants.EVENT_CODE_KEYWORD, eventCode);
		payload.put(Constants.USER_KEYWORD, username);
		payload.put(Constants.PRIORITY_VALUE_KEYWORD, priority);
		String response = sendMessage(Constants.MESSAGE_TYPE_ADD_PRIORITY, payload);
		return response;
	}

	private String generateSalt() {
		byte[] salt = new byte[32];
		fRandomGenerator.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}
}
