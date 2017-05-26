package com.eventprogramming.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.eventprogramming.constants.Constants;
import com.eventprogramming.database.MySQLAccess;
import com.eventprogramming.utils.Utils;

public class ServerConnection {
	
	private static final Thread fHelloThread = new Thread("HelloJob") {

		private String allConnectionsJSON = "{\"hello\" : 6789,"
										+ " \"create-user\" : 6790,"
										+ " \"login\" : 6791,"
										+ " \"create-event-interval\" : 6793,"
										+ " \"create-event\" : 6792}" + '\n';
		private ServerSocket welcomeSocket;
		
		@Override
		public void run() {
			String clientSentence;
			try {
				welcomeSocket = new ServerSocket(6789);
				while (true) {
					Socket connectionSocket = welcomeSocket.accept();
					BufferedReader inFromClient = new BufferedReader(
							new InputStreamReader(connectionSocket.getInputStream()));
					DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
					clientSentence = inFromClient.readLine();
					System.out.println("Received: " + clientSentence);
					outToClient.writeBytes(allConnectionsJSON);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					welcomeSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	private static final Thread fLoginThread = new Thread("login") {
		
		private final MySQLAccess fSQLAccess = new MySQLAccess();
		private ServerSocket serverSocket;
		
		@Override
		public void run() {
			String clientSentence;
			try {					
				serverSocket = new ServerSocket(6791);
				while (true) {
					Socket connectionSocket = serverSocket.accept();
					BufferedReader inFromClient = new BufferedReader(
							new InputStreamReader(connectionSocket.getInputStream()));
					DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
					clientSentence = inFromClient.readLine();
					System.out.println("Received: " + clientSentence);
					String response = acceptLogin(clientSentence) + '\n';
					outToClient.writeBytes(response);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private String acceptLogin(String clientSentence) {
			try {
				JSONObject json = (JSONObject) new JSONParser().parse(clientSentence);
				String username = (String) json.get(Constants.USER_KEYWORD);
				String password = (String) json.get(Constants.PASS_KEYWORD);
				
				return fSQLAccess.acceptLogin(username, password);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			return "ERROR";
		};
		
	};
	
	private static final Thread fCreateUserThread = new Thread("create-user") {

		private final MySQLAccess fSQLAccess = new MySQLAccess();
		private ServerSocket serverSocket;
		
		@Override
		public void run() {
			String clientSentence;
			try {					
				serverSocket = new ServerSocket(6790);
				while (true) {
					Socket connectionSocket = serverSocket.accept();
					BufferedReader inFromClient = new BufferedReader(
							new InputStreamReader(connectionSocket.getInputStream()));
					DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
					clientSentence = inFromClient.readLine();
					System.out.println("Received: " + clientSentence);
					String response = createUser(clientSentence) + '\n';
					outToClient.writeBytes(response);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private String createUser(String clientSentence) {
			try {
				JSONObject json = (JSONObject) new JSONParser().parse(clientSentence);
				String username = (String) json.get(Constants.USER_KEYWORD);
				String email = (String) json.get(Constants.EMAIL_KEYWORD);
				String password = (String) json.get(Constants.PASS_KEYWORD);
				String salt = (String) json.get(Constants.SALT_KEYWORD);
				
				// Process password
				password += salt;
				MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
				messageDigest.update(password.getBytes());
				String encryptedPassword = new String(messageDigest.digest());
				
				return fSQLAccess.insertUser(username, encryptedPassword, email, salt);
			} catch (ParseException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			
			return "ERROR";
		};
	};
	
	private static final Thread fCreateEventThread = new Thread("create-event") {

		private final MySQLAccess fSQLAccess = new MySQLAccess();
		private ServerSocket serverSocket;
		
		@Override
		public void run() {
			String clientSentence;
			try {					
				serverSocket = new ServerSocket(6792);
				while (true) {
					Socket connectionSocket = serverSocket.accept();
					BufferedReader inFromClient = new BufferedReader(
							new InputStreamReader(connectionSocket.getInputStream()));
					DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
					clientSentence = inFromClient.readLine();
					System.out.println("Received: " + clientSentence);
					String response = createEvent(clientSentence) + '\n';
					outToClient.writeBytes(response);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private String createEvent(String clientSentence) {
			try {
				JSONObject json = (JSONObject) new JSONParser().parse(clientSentence);
				String eventName	= (String) json.get(Constants.EVENT_NAME_KEYWORD);
				int isGreedy 		= ((Long)json.get(Constants.GREEDY_KEYWORD)).intValue();
				String startDate	= (String) json.get(Constants.MIN_START_DATE_KEYWORD);
				String endDate		= (String) json.get(Constants.MAX_END_DATE_KEYWORD);
				int startHour		= ((Long)json.get(Constants.START_HOUR_KEYWORD)).intValue();
				int endHour	 		= ((Long)json.get(Constants.END_HOUR_KEYWORD)).intValue();
				int duration     	= ((Long)json.get(Constants.DURATION_KEYWORD)).intValue();
				String username		= (String) json.get(Constants.USER_KEYWORD);
				String eventCode	= String.valueOf(Math.abs(clientSentence.hashCode()));
				// Process password
				
				return fSQLAccess.insertEvent(eventName,
									isGreedy,
									startDate,
									endDate,
									startHour,
									endHour,
									duration,
									username,
									eventCode);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			return "ERROR";
		}
	};
	
	private static final Thread fCreateEventIntervalThread = new Thread("create-event-interval") {

		private final MySQLAccess fSQLAccess = new MySQLAccess();
		private ServerSocket serverSocket;
		
		@Override
		public void run() {
			String clientSentence;
			try {					
				serverSocket = new ServerSocket(6793);
				while (true) {
					Socket connectionSocket = serverSocket.accept();
					BufferedReader inFromClient = new BufferedReader(
							new InputStreamReader(connectionSocket.getInputStream()));
					DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
					clientSentence = inFromClient.readLine();
					System.out.println("Received: " + clientSentence);
					String response = createEventInterval(clientSentence) + '\n';
					outToClient.writeBytes(response);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private String createEventInterval(String clientSentence) {
			try {
				JSONObject json = (JSONObject) new JSONParser().parse(clientSentence);
				String eventCode	= (String) json.get(Constants.EVENT_CODE_KEYWORD);
				String date	= (String) json.get(Constants.DATE_KEYWORD);
				int startHour		= ((Long)json.get(Constants.START_HOUR_KEYWORD)).intValue();
				int endHour		= ((Long)json.get(Constants.END_HOUR_KEYWORD)).intValue();
				
				return fSQLAccess.insertEventInterval(eventCode, date, startHour, endHour);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			return "ERROR";
		}
	};
	
	public static void main(String[] args) {
		fHelloThread.start();
		fCreateUserThread.start();
		fLoginThread.start();
		fCreateEventThread.start();
		fCreateEventIntervalThread.start();
	}
	
}
