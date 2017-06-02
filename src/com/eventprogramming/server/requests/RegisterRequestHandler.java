package com.eventprogramming.server.requests;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.simple.JSONObject;

import com.eventprogramming.constants.Constants;

public class RegisterRequestHandler extends AbstractRequestHandler {

	@Override
	public String handleRequest(JSONObject payload) {
		try {
			String username = (String) payload.get(Constants.USER_KEYWORD);
			String email = (String) payload.get(Constants.EMAIL_KEYWORD);
			String password = (String) payload.get(Constants.PASS_KEYWORD);
			String salt = (String) payload.get(Constants.SALT_KEYWORD);

			// Process password
			password += salt;
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(password.getBytes());
			String encryptedPassword = new String(messageDigest.digest());

			return fSQLAccess.insertUser(username, encryptedPassword, email, salt);
		} catch (ClassCastException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return Constants.SERVER_ERROR;
	}

}
