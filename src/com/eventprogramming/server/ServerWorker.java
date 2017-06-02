package com.eventprogramming.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.eventprogramming.constants.Constants;
import com.eventprogramming.server.requests.AbstractRequestHandler;
import com.eventprogramming.server.requests.RequestHandler;

public class ServerWorker implements Runnable {

	private Socket connectionSocket;
	private Map<String, RequestHandler> requestHandlers;
	
	public ServerWorker(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
		requestHandlers = AbstractRequestHandler.getAllHandlers();
	}
	
	@Override
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			String clientMessage = inFromClient.readLine();
			System.out.println("Received: " + clientMessage);
			String response = treatClientMessage(clientMessage);
			outToClient.writeBytes(response + '\n');
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				connectionSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String treatClientMessage(String clientMessage) {
		try {
			JSONObject message = (JSONObject) new JSONParser().parse(clientMessage);
			
			String messageType = parseMessageType(message);
			if (messageType == null)
				return Constants.SERVER_ERROR;
			
			JSONObject payload = parseMessagePayload(message);
			if (payload == null)
				return Constants.SERVER_ERROR;
			
			RequestHandler handler = requestHandlers.get(messageType);
			if (handler == null)
				return Constants.SERVER_ERROR;
			
			return handler.handleRequest(payload);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return Constants.SERVER_ERROR;
	}

	private String parseMessageType(JSONObject json) {
		boolean validMessage = json.containsKey(Constants.MESSAGE_TYPE);
		if (validMessage)
			return (String) json.get(Constants.MESSAGE_TYPE);
		else
			return null;
	}
	
	private JSONObject parseMessagePayload(JSONObject json) {
		boolean validMessage = json.containsKey(Constants.MESSAGE_PAYLOAD);
		if (validMessage)
			return (JSONObject) json.get(Constants.MESSAGE_PAYLOAD);
		else
			return null;
	}

}
