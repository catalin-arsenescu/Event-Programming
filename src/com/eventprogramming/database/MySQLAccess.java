package com.eventprogramming.database;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.swt.widgets.DateTime;
import org.json.simple.JSONObject;

import com.eventprogramming.constants.Constants;
import com.eventprogramming.utils.Utils;

public class MySQLAccess {
	private Connection fConnect = null;
	private ResultSet fResultSet = null;
	private PreparedStatement fPreparedStatement = null;

	public MySQLAccess() {
		// This will load the MySQL driver, each DB has its own driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			fConnect = DriverManager
					.getConnection(Constants.DB_SCHEMA_ADDRESS + Constants.DB_CREDENTIALS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readDataBase() throws Exception {
		try {
			
//			// Statements allow to issue SQL queries to the database
//			statement = fConnect.createStatement();
//			// Result set get the result of the SQL query
//			resultSet = statement.executeQuery("select * from feedback.comments");
//			writeResultSet(resultSet);

//			// PreparedStatements can use variables and are more efficient
//			preparedStatement = connect
//					.prepareStatement("insert into  feedback.comments values (default, ?, ?, ?, ? , ?, ?)");
//			// "myuser, webpage, datum, summary, COMMENTS from
//			// feedback.comments");
//			// Parameters start with 1
//			preparedStatement.setString(1, "Test");
//			preparedStatement.setString(2, "TestEmail");
//			preparedStatement.setString(3, "TestWebpage");
//			preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
//			preparedStatement.setString(5, "TestSummary");
//			preparedStatement.setString(6, "TestComment");
//			preparedStatement.executeUpdate();
//
//			preparedStatement = connect
//					.prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from feedback.comments");
//			resultSet = preparedStatement.executeQuery();
//			writeResultSet(resultSet);
//
//			// Remove again the insert comment
//			preparedStatement = connect.prepareStatement("delete from feedback.comments where myuser= ? ; ");
//			preparedStatement.setString(1, "Test");
//			preparedStatement.executeUpdate();
//
//			resultSet = statement.executeQuery("select * from feedback.comments");
//			writeMetaData(resultSet);

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}

	/**
	 * Accepts an user login
	 */
	public String acceptLogin(String username, String password) {
		try {

			Statement statement = fConnect.createStatement();
			// Result set get the result of the SQL query
			String command = "select * from feedback.epuser "
					+ "where username=" + "\"" + username +"\"";
			ResultSet resultSet = statement.executeQuery(command);
			if (!resultSet.next())
				return "ERROR";
			
			String DBUser = resultSet.getString("username");
			String DBPassword = resultSet.getString("password");
			String DBSalt = resultSet.getString("salt");
			
			// Verify password
			password += DBSalt;
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(password.getBytes());
			String encryptedPassword = new String(messageDigest.digest());
			
			boolean passwordOK = DBPassword.equals(encryptedPassword);
			if (!passwordOK)
				return "ERROR";
			
			return getEventsForUsername(username).toJSONString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	private JSONObject getEventsForUsername(String username) throws SQLException {
		String command;
		ResultSet resultSet;
		Statement statement = fConnect.createStatement();
		
		command = "select * from feedback.ep_event "
				+ "where initiator=" + "\"" + username +"\"";
		resultSet = statement.executeQuery(command);
		JSONObject result = new JSONObject();
		int i = 1;
		while (resultSet.next()) {
			JSONObject event = new JSONObject();
			
			String name = resultSet.getString("event_name");
			event.put(Constants.EVENT_NAME_KEYWORD, name);
			
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
		
		return result;
	}
	
	/**
	 * Inserts event into database
	 * TODO check if event exists
	 */
	public String insertEvent(String eventName, int isGreedy, String startDate, String endDate,
			int startHour, int endHour, int duration, String username, String eventCode) {
		try {
			fPreparedStatement = fConnect.prepareStatement("insert into feedback.ep_event"
					+ " (is_greedy, start_date, end_date, start_hour, end_hour, duration, initiator, event_code, event_name)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			fPreparedStatement.setInt(1, isGreedy);
			fPreparedStatement.setString(2, startDate);
			fPreparedStatement.setString(3, endDate);
			fPreparedStatement.setInt(4, startHour);
			fPreparedStatement.setInt(5, endHour);
			fPreparedStatement.setInt(6, duration);
			fPreparedStatement.setString(7, username);
			fPreparedStatement.setString(8, eventCode);
			fPreparedStatement.setString(9, eventName);
			fPreparedStatement.executeUpdate();
			
			return eventCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "ERROR";
	}
	
	/**
	 * Inserts event into database
	 * TODO check if event exists
	 */
	public String insertEventInterval(String eventCode, String date, int startHour, int endHour) {
		try {
			int eventId = getIdFromEventCode(eventCode);
			if (eventId < 0)
				return "ERROR";
			
			fPreparedStatement = fConnect.prepareStatement("insert into feedback.ep_greedy_intervals"
					+ " (event_id, date, start_hour, end_hour)"
					+ " values (?, ?, ?, ?)");
			fPreparedStatement.setInt(1, eventId);
			fPreparedStatement.setString(2, date);
			fPreparedStatement.setInt(3, startHour);
			fPreparedStatement.setInt(4, endHour);
			fPreparedStatement.executeUpdate();
			
			return "OK";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "ERROR";
	}

	private int getIdFromEventCode(String eventCode) {

		 try {
			PreparedStatement preparedStatement = fConnect.prepareStatement("select event_id from feedback.ep_event where event_code=?;");
			preparedStatement.setString(1, eventCode);
			ResultSet executeQuery = preparedStatement.executeQuery();
			while (executeQuery.next()) {
				return executeQuery.getInt("event_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Inserts user into database
	 * TODO check if username or email exists
	 */
	public String insertUser(String user, String password, String email, String salt) {
		try {
			fPreparedStatement = fConnect.prepareStatement("insert into  feedback.epuser values (?, ?, ?, ?)");
			fPreparedStatement.setString(1, user);							// Name
			fPreparedStatement.setString(2, password);						// Encrypted password
			fPreparedStatement.setString(3, email);							// E-mail
			fPreparedStatement.setString(4, salt);							// Salt
			fPreparedStatement.executeUpdate();
			
			return "OK";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "ERROR";
	}

	private void writeMetaData(ResultSet resultSet) throws SQLException {
		// Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
			System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			String user = resultSet.getString("username");
			String password = resultSet.getString("password");
			String email = resultSet.getString("email");
			String salt = resultSet.getString("salt");
//			System.out.println("User: " + user);
//			System.out.println("Password: " + password);
//			System.out.println("Email: " + email);
//			System.out.println("Salt: " + salt);
		}
	}

	// You need to close the resultSet
	private void close() {
		try {
			if (fResultSet != null) {
				fResultSet.close();
			}

			if (fPreparedStatement != null) {
				fPreparedStatement.close();
			}

			if (fConnect != null) {
				fConnect.close();
			}
		} catch (Exception e) {

		}
	}
}