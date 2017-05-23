package com.eventprogramming.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.crypto.SecretKeyFactory;

import org.eclipse.swt.widgets.DateTime;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.eventprogramming.constants.Constants;
import com.eventprogramming.utils.Utils;

public class ClientConnection {

	private ClientGUI fClientGUI;
	private final SecureRandom fRandomGenerator;
	private JSONObject fServicePorts;

	public ClientConnection(ClientGUI clientGUI) {
		this.fClientGUI = clientGUI;
		fRandomGenerator = new SecureRandom(
				ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(System.nanoTime()).array());
		sendHello();
	}

	public void sendHello() {
		Socket clientSocket;
		try {
			clientSocket = new Socket("localhost", 6789);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer.writeBytes("HELLO" + '\n');
			String response = inFromServer.readLine();
			System.out.println("FROM SERVER: " + response);
			
			// Parse JSON response and save services
			fServicePorts = (JSONObject) new JSONParser().parse(response);
			clientSocket.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public boolean sendLogin(String username, String password) {
		int port = getPort(Constants.LOGIN_SERVICE);
		if (port < 0) {
			fClientGUI.reportError(Constants.SERVER_OFFLINE_ERROR);
			return false;
		}
		
		JSONObject json = new JSONObject();
		json.put(Constants.USER_KEYWORD, username);
		json.put(Constants.PASS_KEYWORD, password);
		String message = json.toJSONString() + '\n';
		System.out.println("SENDING" + message);
		
		Socket clientSocket;
		try {
			clientSocket = new Socket("localhost", port);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer.writeBytes(message);
			String response = inFromServer.readLine();
			clientSocket.close();
			System.out.println("FROM SERVER: " + response);
			if ("ERROR".equals(response)) {
				return false;
			} else {
				fClientGUI.fEventCache.addEvents(response);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@SuppressWarnings({"unchecked"})
	public boolean sendNewUserCredentials(String username, String password, String email) {
		int port = getPort(Constants.CREATE_USER_SERVICE);
		if (port < 0) {
			fClientGUI.reportError(Constants.SERVER_OFFLINE_ERROR);
			return false;
		}
		
		JSONObject json = new JSONObject();
		json.put(Constants.USER_KEYWORD, username);
		json.put(Constants.PASS_KEYWORD, password);
		json.put(Constants.EMAIL_KEYWORD, email);
		json.put(Constants.SALT_KEYWORD, generateSalt());
		String message = json.toJSONString() + '\n';
		System.out.println("SENDING" + message);
		
		Socket clientSocket;
		try {
			clientSocket = new Socket("localhost", port);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer.writeBytes(message);
			String response = inFromServer.readLine();
			clientSocket.close();
			System.out.println("FROM SERVER: " + response);
			if ("OK".equals(response))
				return true;
			else 
				return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	private int getPort(String serviceName) {
		if (fServicePorts == null)
			return -1;
		
		Long queryResult = (Long)fServicePorts.get(serviceName);
		if (queryResult == null)
			return -1;
		
		return queryResult.intValue();
	}

	private String generateSalt() {
		byte[] salt = new byte[32];
		fRandomGenerator.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	@SuppressWarnings("unchecked")
	public String sendNewEvent(String eventName, boolean isGreedy, DateTime startDate, DateTime endDate,
			int startHour, int endHour, int duration, String username) {

		int port = getPort(Constants.CREATE_EVENT_SERVICE);
		if (port < 0) {
			fClientGUI.reportError(Constants.SERVER_OFFLINE_ERROR);
			return "ERROR";
		}
		
		JSONObject json = new JSONObject();
		json.put(Constants.EVENT_NAME_KEYWORD, eventName);
		json.put(Constants.GREEDY_KEYWORD, isGreedy ? 1 : 0);
		json.put(Constants.MIN_START_DATE_KEYWORD, Utils.getDateString(startDate));
		json.put(Constants.MAX_END_DATE_KEYWORD, Utils.getDateString(endDate));
		json.put(Constants.START_HOUR_KEYWORD, startHour);
		json.put(Constants.END_HOUR_KEYWORD, endHour);
		json.put(Constants.DURATION_KEYWORD, duration);
		json.put(Constants.USER_KEYWORD, username);
		
		String message = json.toJSONString() + '\n';
		System.out.println("SENDING" + message);
		
		Socket clientSocket;
		try {
			clientSocket = new Socket("localhost", port);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer.writeBytes(message);
			String response = inFromServer.readLine();
			clientSocket.close();
			System.out.println("FROM SERVER: " + response);
			return response;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "ERROR";
	}
	
	@SuppressWarnings("unchecked")
	public String sendNewEventInterval(String eventCode, DateTime date, int startHour) {

		int port = getPort(Constants.CREATE_EVENT_INTERVAL_SERVICE);
		if (port < 0) {
			fClientGUI.reportError(Constants.SERVER_OFFLINE_ERROR);
			return "ERROR";
		}
		
		JSONObject json = new JSONObject();
		json.put(Constants.EVENT_CODE_KEYWORD, eventCode);
		json.put(Constants.DATE_KEYWORD, Utils.getDateString(date));
		json.put(Constants.START_HOUR_KEYWORD, startHour);
		
		String message = json.toJSONString() + '\n';
		System.out.println("SENDING" + message);
		
		Socket clientSocket;
		try {
			clientSocket = new Socket("localhost", port);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer.writeBytes(message);
			String response = inFromServer.readLine();
			clientSocket.close();
			System.out.println("FROM SERVER: " + response);
			return response;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "ERROR";
	}
}
