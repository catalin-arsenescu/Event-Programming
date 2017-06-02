package com.eventprogramming.database;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONObject;

import com.eventprogramming.constants.Constants;
import com.eventprogramming.utils.Utils;

public class MySQLAccess {
	private Connection fConnect = null;
	private ResultSet fResultSet = null;
	private PreparedStatement statement = null;

	public MySQLAccess() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			fConnect = DriverManager.getConnection(Constants.DB_SCHEMA_ADDRESS + Constants.DB_CREDENTIALS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Accepts an user login
	 */
	public String login(String username, String password) {
		try {

			String command = "select * from feedback.epuser where username=?";
			PreparedStatement statement = fConnect.prepareStatement(command);
			statement.setString(1, username);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return Constants.SERVER_ERROR;

			String DBPassword = resultSet.getString("password");
			String DBSalt = resultSet.getString("salt");

			// Verify password
			password += DBSalt;
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(password.getBytes());
			String encryptedPassword = new String(messageDigest.digest());

			boolean passwordOK = DBPassword.equals(encryptedPassword);
			if (!passwordOK)
				return Constants.SERVER_ERROR;

			return getEventsForUsername(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private String getEventsForUsername(String username) throws SQLException {
		String command = "select * from feedback.ep_event where initiator=?";
		PreparedStatement statement = fConnect.prepareStatement(command);
		statement.setString(1, username);
		ResultSet resultSet = statement.executeQuery();
		
		JSONObject result = new JSONObject();
		int i = 1;
		while (resultSet.next()) {
			JSONObject event = new JSONObject();

			String name = resultSet.getString("event_name");
			event.put(Constants.EVENT_NAME_KEYWORD, name);

			String initiator = resultSet.getString("initiator");
			event.put(Constants.USER_KEYWORD, initiator);
			
			int type = resultSet.getInt("is_greedy");
			event.put(Constants.GREEDY_KEYWORD, type);

			java.sql.Date startDate = resultSet.getDate("start_date");
			event.put(Constants.MIN_START_DATE_KEYWORD, Utils.getDateString(startDate));

			java.sql.Date endDate = resultSet.getDate("end_date");
			event.put(Constants.MAX_END_DATE_KEYWORD, Utils.getDateString(endDate));

			int startHour = resultSet.getInt("start_hour");
			event.put(Constants.START_HOUR_KEYWORD, startHour);

			int endHour = resultSet.getInt("end_hour");
			event.put(Constants.END_HOUR_KEYWORD, endHour);

			int duration = resultSet.getInt("duration");
			event.put(Constants.DURATION_KEYWORD, duration);

			String eventCode = resultSet.getString("event_code");
			event.put(Constants.EVENT_CODE_KEYWORD, eventCode);

			result.put(Constants.EVENT_KEYWORD + i++, event);
		}

		return result.toJSONString();
	}

	/**
	 * Inserts event into database
	 */
	public String insertEvent(String eventName, int isGreedy, String startDate, String endDate, int startHour,
			int endHour, int duration, String username, String eventCode) {
		
		if (eventExists(eventName))
			return Constants.SERVER_ERROR;
		
		try {
			String command = "insert into feedback.ep_event"
					+ " (is_greedy, start_date, end_date, start_hour, "
					+ "end_hour, duration, initiator, event_code, event_name)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = fConnect.prepareStatement(command);
			statement.setInt(1, isGreedy);
			statement.setString(2, startDate);
			statement.setString(3, endDate);
			statement.setInt(4, startHour);
			statement.setInt(5, endHour);
			statement.setInt(6, duration);
			statement.setString(7, username);
			statement.setString(8, eventCode);
			statement.setString(9, eventName);
			statement.executeUpdate();

			return eventCode;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Constants.SERVER_ERROR;
	}

	private boolean eventExists(String eventName) {
		try {
			
			String command = "select * from feedback.ep_event where event_name=?";
			PreparedStatement statement = fConnect.prepareStatement(command);
			statement.setString(1, eventName);
			ResultSet result = statement.executeQuery();
			
			return result.next();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	/**
	 * Inserts event interval into database
	 */
	public String insertEventInterval(String eventCode, String date, int startHour, int endHour) {
		try {
			int eventID = getIdFromEventCode(eventCode);
			if (eventID < 0)
				return Constants.SERVER_ERROR;
			
			if (intervalExists(eventID, date, startHour, endHour))
				return Constants.SERVER_ERROR;

			String command = "insert into feedback.ep_greedy_intervals (event_id, date, start_hour, end_hour)" + " values (?, ?, ?, ?)";
			PreparedStatement statement = fConnect.prepareStatement(command);
			statement.setInt(1, eventID);
			statement.setString(2, date);
			statement.setInt(3, startHour);
			statement.setInt(4, endHour);
			statement.executeUpdate();

			return Constants.SERVER_OK;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Constants.SERVER_ERROR;
	}

	private boolean intervalExists(int eventID, String date, int startHour, int endHour) {
		try {
			
			String command = "select * from feedback.ep_greedy_intervals where event_id=? AND date=? AND start_hour=? AND end_hour=?";
			PreparedStatement statement = fConnect.prepareStatement(command);
			statement.setInt(1, eventID);
			statement.setString(2, date);
			statement.setInt(3, startHour);
			statement.setInt(4, endHour);
			ResultSet result = statement.executeQuery();
			
			return result.next();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	private int getIdFromEventCode(String eventCode) {

		try {
			PreparedStatement preparedStatement = fConnect
					.prepareStatement("select event_id from feedback.ep_event where event_code=?");
			preparedStatement.setString(1, eventCode);
			ResultSet executeQuery = preparedStatement.executeQuery();
			while (executeQuery.next())
				return executeQuery.getInt("event_id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int getEventIdFromIntervalId(int intervalId) {

		try {
			PreparedStatement preparedStatement = fConnect
					.prepareStatement("select event_id from feedback.ep_greedy_intervals where interval_id=?;");
			preparedStatement.setInt(1, intervalId);
			ResultSet executeQuery = preparedStatement.executeQuery();
			while (executeQuery.next())
				return executeQuery.getInt("event_id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Inserts user into database TODO check if username or email exists
	 */
	public String insertUser(String user, String password, String email, String salt) {
		try {
			
			String command = "insert into  feedback.epuser values (?, ?, ?, ?)";
			PreparedStatement statement = fConnect.prepareStatement(command);
			statement.setString(1, user); // Name
			statement.setString(2, password); // Encrypted password
			statement.setString(3, email); // E-mail
			statement.setString(4, salt); // Salt
			statement.executeUpdate();

			return Constants.SERVER_OK;
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Constants.SERVER_ERROR;
	}

	@SuppressWarnings("unchecked")
	public JSONObject getPrioritiesForEvent(String eventCode) {
		try {
			String command = "select * from feedback.ep_priority where event_code=?";
			PreparedStatement statement = fConnect.prepareStatement(command);
			statement.setString(1, eventCode);
			ResultSet resultSet = statement.executeQuery();
			JSONObject result = new JSONObject();
			int i = 1;
			
			while (resultSet.next()) {
				
				JSONObject priority = new JSONObject();
				
				String username = resultSet.getString("username");
				priority.put(Constants.USER_KEYWORD, username);

				int priorityValue = resultSet.getInt("priority");
				priority.put(Constants.PRIORITY_VALUE_KEYWORD, priorityValue);

				result.put(Constants.PRIORITY_KEYWORD + i++, priority);
			}

			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new JSONObject();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getVotesForEvent(String eventCode) {
		JSONObject intervals = getEventIntervals(eventCode);
		JSONObject interval;
		JSONObject result = new JSONObject();
		int i = 1;
		while ((interval = (JSONObject) intervals.get(Constants.INTERVAL_KEYWORD + i++)) != null) {
			int intervalId = (int) interval.get(Constants.INTERVAL_ID_KEYWORD);
			result.put(Constants.INTERVAL_KEYWORD + (i-1), getVotesForInterval(intervalId));
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getVotesForInterval(int intervalId) {
		ResultSet resultSet;
		String command = "select * from feedback.ep_vote " + "where interval_id=" + intervalId;
		try {
			Statement statement = fConnect.createStatement();
			resultSet = statement.executeQuery(command);
			JSONObject result = new JSONObject();
			int i = 1;
			while (resultSet.next()) {
				JSONObject vote = new JSONObject();

				String username = resultSet.getString("username");
				vote.put(Constants.USER_KEYWORD, username);

				int interval_id = resultSet.getInt("interval_id");
				vote.put(Constants.INTERVAL_ID_KEYWORD, interval_id);

				int voteId = resultSet.getInt("vote_id");
				vote.put(Constants.VOTE_ID_KEYWORD, voteId);

				int voteType = resultSet.getInt("vote_type");
				vote.put(Constants.VOTE_TYPE_KEYWORD, voteType);

				result.put(Constants.VOTE_KEYWORD + i++, vote);
			}
			
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new JSONObject();
	}

	public JSONObject getEventIntervals(String eventCode) {
		return getEventIntervals(eventCode, null);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getEventIntervals(String eventCode, String username) {
		int eventId = getIdFromEventCode(eventCode);
		ResultSet resultSet;
		String command = "select * from feedback.ep_greedy_intervals " + "where event_id=" + eventId;
		try {
			Statement statement = fConnect.createStatement();
			resultSet = statement.executeQuery(command);
			JSONObject result = new JSONObject();
			int i = 1;
			while (resultSet.next()) {
				JSONObject interval = new JSONObject();

				String date = resultSet.getString("date");
				interval.put(Constants.DATE_KEYWORD, date);

				int interval_id = resultSet.getInt("interval_id");
				interval.put(Constants.INTERVAL_ID_KEYWORD, interval_id);

				int startHour = resultSet.getInt("start_hour");
				interval.put(Constants.START_HOUR_KEYWORD, startHour);

				int endHour = resultSet.getInt("end_hour");
				interval.put(Constants.END_HOUR_KEYWORD, endHour);
				
				int existingVote = getVoteTypeForEventAndUsername(interval_id, username);
				interval.put(Constants.VOTE_KEYWORD, existingVote);

				result.put(Constants.INTERVAL_KEYWORD + i++, interval);
			}
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new JSONObject();
	}

	public int getVoteTypeForEventAndUsername(int intervalID, String username) {
		ResultSet resultSet;
		PreparedStatement statement;
		try {
			statement = fConnect.prepareStatement("select * from ep_vote where interval_id=? and username=?");
			statement.setInt(1, intervalID);
			statement.setString(2, username);
			statement.execute();
			
			resultSet = statement.getResultSet();
			if (resultSet != null && resultSet.first())
				return resultSet.getInt("vote_type");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 2; // DEFAULT IFB
	}
	
	@SuppressWarnings("unchecked")
	public String getEventForCode(String eventCode) {
		String command;
		ResultSet resultSet;
		Statement statement;
		try {
			statement = fConnect.createStatement();
			command = "select * from feedback.ep_event " + "where event_code=" + "\"" + eventCode + "\"";
			resultSet = statement.executeQuery(command);
			JSONObject result = new JSONObject();

			while (resultSet.next()) {
				String name = resultSet.getString("event_name");
				result.put(Constants.EVENT_NAME_KEYWORD, name);

				int type = resultSet.getInt("is_greedy");
				result.put(Constants.GREEDY_KEYWORD, type);

				java.sql.Date startDate = resultSet.getDate("start_date");
				result.put(Constants.MIN_START_DATE_KEYWORD, Utils.getDateString(startDate));

				java.sql.Date endDate = resultSet.getDate("end_date");
				result.put(Constants.MAX_END_DATE_KEYWORD, Utils.getDateString(endDate));

				int startHour = resultSet.getInt("start_hour");
				result.put(Constants.START_HOUR_KEYWORD, startHour);

				int endHour = resultSet.getInt("end_hour");
				result.put(Constants.END_HOUR_KEYWORD, endHour);

				int duration = resultSet.getInt("duration");
				result.put(Constants.DURATION_KEYWORD, duration);

				result.put(Constants.EVENT_CODE_KEYWORD, eventCode);

				return result.toJSONString();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Constants.SERVER_ERROR;
	}

	public void addVote(int eventId, int intervalId, String username, int voteType) {
		if (voteExists(eventId, intervalId, username)) {
			updateVote(eventId, intervalId, username, voteType);
			return;
		}	
		
		PreparedStatement statement;
		try {
			statement = fConnect.prepareStatement("insert into feedback.ep_vote(event_id, interval_id, username, vote_type) values(?, ?, ?, ?)");
			statement.setInt(1, eventId);
			statement.setInt(2, intervalId);
			statement.setString(3, username);
			statement.setInt(4, voteType);
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void updateVote(int eventId, int intervalId, String username, int voteType) {
		PreparedStatement statement;
		try {
			statement = fConnect.prepareStatement(
					"UPDATE feedback.ep_vote SET vote_type=? where event_id=? AND interval_id=? AND username=?");
			statement.setInt(1, voteType);
			statement.setInt(2, eventId);
			statement.setInt(3, intervalId);
			statement.setString(4, username);
			statement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private boolean voteExists(int eventId, int intervalId, String username) {
		ResultSet resultSet;
		PreparedStatement statement;
		try {
			statement = fConnect.prepareStatement(
					"select * from feedback.ep_vote where event_id=? AND interval_id=? AND username=?");
			statement.setInt(1, eventId);
			statement.setInt(2, intervalId);
			statement.setString(3, username);
			
			resultSet = statement.executeQuery();
			if (resultSet == null)
				return false;
			
			return resultSet.first();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	public String addPriority(String eventCode, String username, int priority) {
		if (userHasPriority(eventCode, username)) {
			return updatePriority(eventCode, username, priority);
		}
		PreparedStatement statement;
		try {
			statement = fConnect.prepareStatement(
					"INSERT INTO feedback.ep_priority(priority, username, event_code) VALUES(?, ?, ?)");
			statement.setInt(1, priority);
			statement.setString(2, username);
			statement.setString(3, eventCode);
			statement.executeUpdate();
			
			return Constants.SERVER_OK;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Constants.SERVER_ERROR;
	}

	private String updatePriority(String eventCode, String username, int priority) {
		PreparedStatement statement;
		try {
			statement = fConnect.prepareStatement(
					"UPDATE feedback.ep_priority SET priority=? where event_code=? AND username=?");
			statement.setInt(1, priority);
			statement.setString(2, eventCode);
			statement.setString(3, username);
			statement.executeUpdate();
			
			return "OK";
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "ERROR";
	}

	private boolean userHasPriority(String eventCode, String username) {
		ResultSet resultSet;
		PreparedStatement statement;
		try {
			statement = fConnect.prepareStatement(
					"select * from feedback.ep_priority where event_code=? AND username=?");
			statement.setString(1, eventCode);
			statement.setString(2, username);
			
			resultSet = statement.executeQuery();
			if (resultSet == null)
				return false;
			
			return resultSet.first();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	public String deleteIntervals(String eventCode) {
		try {
			int id = getIdFromEventCode(eventCode);
			if (id < 0)
				return Constants.SERVER_ERROR;
			
			String command = "delete from feedback.ep_greedy_intervals where event_id=?";
			PreparedStatement statement = fConnect.prepareStatement(command);
			statement.setInt(1, id);
			statement.executeUpdate();
			
			command = "delete from feedback.ep_vote where event_id=?";
			statement = fConnect.prepareStatement(command);
			statement.setInt(1, id);
			statement.executeUpdate();
			
			return Constants.SERVER_OK;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}