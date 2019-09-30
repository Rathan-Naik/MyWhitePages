package com.mywhitepages.storagemanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.jamonapi.utils.Logger;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import models.Address;
import models.PhoneNumber;
import models.User;
import models.UserProfile;

/**
 * This Class is intended to DB related activities. This class has necessary
 * methods which provides DB Operation.
 */
public class DBConnection {

	private static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final static String DBURL = "jdbc:mysql://localhost/mywhitepages?characterEncoding=latin1";
	private final static String DB_USERNAME = "mywhitepagesapp";
	private final static String DB_PASSWORD = "mywhitepagesapp";

	/**
	 * Creates a new DB connection.
	 *
	 * @return the connection object
	 */
	private static Connection createConnection() {
		Connection connection = null;
		try {
			try {
				Class.forName(MYSQL_JDBC_DRIVER);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			connection = DriverManager.getConnection(DBURL, DB_USERNAME, DB_PASSWORD);
		} catch (Exception e) {
			Logger.logInfo("Failed to Establish connection with DB");
		}
		return connection;
	}

	/**
	 * Registers a new user.
	 * 
	 *
	 * @param user the user bject
	 * @throws UserRegisterdException when the user is already registerd
	 */
	public static void registerUser(User user) throws UserRegisterdException {

		String SQL = "INSERT INTO Users (first_name,last_name,email,password) " + "VALUES(?,?,?,?)";
		long id;
		try {
			Connection conn = createConnection();
			PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, user.getFirstName());
			pstmt.setString(2, user.getLastName());
			pstmt.setString(3, user.getEmail());
			pstmt.setString(4, user.getPassword());



			boolean success = pstmt.execute();

		} catch (MySQLIntegrityConstraintViolationException me) {
			throw new UserRegisterdException();
		} catch (SQLException ex) {
			System.out.println("Failed to add user " + ex.getMessage());
		}
	}

	/**
	 * Gets the user with the email, Returns null if the user is not registered
	 *
	 * @param email the emailid which has to be searched
	 * @return the user
	 */
	public static User getUser(String email) {

		User user = null;

		String SQL = "SELECT * from Users where email=?";
		Connection conn = createConnection();
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, email);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				user = new User(rs.getInt("userid"), rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("email"), rs.getString("password"));
			}

			rs.close();
		} catch (Exception e) {
			Logger.log("Exception occured while getting the user-" + email + e);
		} finally {
			closeDbResources(conn, pstmt);
		}
		return user;
	}

	/**
	 * Gets the user with the specific userid
	 *
	 * @param userid the userid
	 * @return the user object
	 */
	public static User getUser(int userid) {

		User user = null;

		String SQL = "SELECT * from Users where userid=?";
		Connection conn = createConnection();
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, userid);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				user = new User(rs.getInt("userid"), rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("email"), rs.getString("password"));
			}

			rs.close();
		} catch (Exception e) {
			Logger.log("Exception occured while getting the user-" + userid + e);

		} finally {
			closeDbResources(conn, pstmt);
		}
		return user;
	}


	/**
	 * Creates an entry in contactsProfile table.
	 *
	 * @param userid the userid
	 * @param userProfile the userprofile object with details
	 */
	public static void createContactProfile(int userid, UserProfile userProfile) {

		String SQL = "INSERT INTO contactsProfile (ownerid,first_name,last_name,email,Address1,Address2,City,State,Country,image,birthdate,wishpriorhours,lastwishtime) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ";

		Connection conn = createConnection();
		PreparedStatement pstmt = null;

		int i = 1;
		try {
			pstmt = conn.prepareStatement(SQL);

			pstmt.setInt(i++, userid);
			pstmt.setString(i++, userProfile.getFirstName());
			pstmt.setString(i++, userProfile.getLastName());
			pstmt.setString(i++, userProfile.getEmail());

			Address address = userProfile.getAddress();
			pstmt.setString(i++, address.getAddrressLine1());
			pstmt.setString(i++, address.getAddrressLine2());
			pstmt.setString(i++, address.getCity());
			pstmt.setString(i++, address.getState());
			pstmt.setString(i++, address.getCountry());
			pstmt.setString(i++, null);

			pstmt.setDate(i++, userProfile.getDob());
			pstmt.setInt(i++, userProfile.getWishPrior());
			pstmt.setDate(i++, null);

			boolean success = pstmt.execute();

		} catch (Exception ex) {
			Logger.log("Failed to create Contact Profile" + ex);
		} finally {
			closeDbResources(conn, pstmt);
		}
	}

	/**
	 * Insert phone numbers into phonenumbers table. Bulk insert.
	 * 
	 * @param numbers
	 *            the list numbers
	 * @throws UserRegisterdException
	 *             the user registerd exception
	 */
	public static void insertPhoneNumber(List<PhoneNumber> numbers) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "INSERT INTO phonenumbers (profileid,phonenumber,Category) VALUES (?,?,?)";
		String values = ",(?,?,?) ";

		try {
			conn = createConnection();

			for (int i = 1; i < numbers.size(); i++) {
				SQL += values;
			}

			pstmt = conn.prepareStatement(SQL);

			int i = 1;
			for (PhoneNumber phNo : numbers) {
				pstmt.setInt(i++, phNo.getProfileId());
				pstmt.setString(i++, phNo.getPhoneNumber());
				pstmt.setString(i++, phNo.getCategory());
			}

			boolean sucess = pstmt.execute();

		} catch (Exception ex) {
			Logger.log("Failed to insert phone numberss" + ex);
		} finally {
			closeDbResources(conn, pstmt);
		}
	}

	/**
	 * View the contact profile.
	 *
	 * @param userid
	 *            the userid
	 * @param orderby
	 *            the orderby column name
	 * @param order
	 *            the order, ascending or descending
	 * @param limit
	 *            the limit of result
	 * @return the list of profiles created by the current user.
	 */
	public static List<UserProfile> viewContactProfile(int userid, String orderby, String order, int limit) {

		String SQL = "SELECT * from contactsProfile where ownerid=? ";
		if (orderby != null) {
			SQL = SQL + "order by " + orderby + " " + order;
		}

		/** 0 limit indicates, do not add limit in the query, fetchall */
		if (limit > 0) {
			SQL = SQL + " limit " + limit;
		}

		Connection conn = createConnection();
		PreparedStatement pstmt = null;

		List<UserProfile> profiles = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, userid);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				UserProfile userprofile = new UserProfile();
				userprofile.setProfileid(rs.getInt("profileid"));
				userprofile.setOwnerid(rs.getInt("ownerid"));
				userprofile.setFirstName(rs.getString("first_name"));
				userprofile.setLastName(rs.getString("last_name"));
				userprofile.setEmail(rs.getString("email"));
				userprofile.setDob(rs.getDate("birthdate"));
				userprofile.setWishPrior(rs.getInt("wishpriorhours"));
				userprofile.setLastWishTime(rs.getTimestamp("lastwishtime"));

				Address address = new Address();
				address.setAddrressLine1(rs.getString("Address1"));
				address.setAddrressLine2(rs.getString("Address2"));
				address.setCity(rs.getString("City"));
				address.setState(rs.getString("State"));
				address.setCountry(rs.getString("Country"));
				userprofile.setAddress(address);

				profiles.add(userprofile);
			}

			rs.close();
		} catch (Exception e) {
			Logger.log("Failed to fetch profiles " + e);
		} finally {

			closeDbResources(conn, pstmt);
		}
		return profiles;
	}

	/**
	 * Fetch all the contact profiles associated with the user
	 *
	 * @param currentUserId
	 *            the current user id
	 * @param orderby
	 *            the orderby
	 * @param order
	 *            the order
	 * @return the list
	 */
	public static List<UserProfile> viewContactProfiles(int currentUserId, String orderby, String order) {
		return viewContactProfile(currentUserId, orderby, order, 0);
	}

	/**
	 * Fetch profile.
	 *
	 * @param profileid
	 *            the profileid
	 * @return the user profile
	 */
	public static UserProfile fetchProfile(int profileid) {

		String SQL = "SELECT * from contactsProfile where profileid=? ";

		Connection conn = createConnection();
		PreparedStatement pstmt = null;

		UserProfile profile = null;
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, profileid);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {

				profile = new UserProfile();
				profile.setProfileid(rs.getInt("profileid"));
				profile.setOwnerid(rs.getInt("ownerid"));
				profile.setFirstName(rs.getString("first_name"));
				profile.setLastName(rs.getString("last_name"));
				profile.setEmail(rs.getString("email"));
				profile.setDob(rs.getDate("birthdate"));
				profile.setWishPrior(rs.getInt("wishpriorhours"));
				profile.setLastWishTime(rs.getTimestamp("lastwishtime"));

				Address address = new Address();
				address.setAddrressLine1(rs.getString("Address1"));
				address.setAddrressLine2(rs.getString("Address2"));
				address.setCity(rs.getString("City"));
				address.setState(rs.getString("State"));
				address.setCountry(rs.getString("Country"));

				profile.setAddress(address);
			}

			rs.close();
		} catch (Exception e) {
			Logger.log("Failed to fetch profiles linked with profileid-"+profileid+e);
		} finally {

			closeDbResources(conn, pstmt);
		}
		return profile;
	}

	/**
	 * Fetch phone numbers.
	 *
	 * @param profileid the profileid
	 * @return the list
	 */
	public static List<PhoneNumber> fetchPhoneNumbers(int profileid) {

		String SQL = "SELECT * from phonenumbers where profileid=? ";

		Connection conn = createConnection();
		PreparedStatement pstmt = null;

		List<PhoneNumber> phoneNumbers = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, profileid);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				PhoneNumber phNo = new PhoneNumber();
				phNo.setPhoneNumber(rs.getString("phonenumber"));
				phNo.setCategory(rs.getString("Category"));
				phNo.setPhoneNumber(rs.getString("phonenumber"));
				phNo.setProfileId(rs.getInt("profileid"));

				phoneNumbers.add(phNo);
			}

			rs.close();
		} catch (Exception e) {
			Logger.log("Failed to fetch phone numbers associated with profileid-"+profileid+e);
		} finally {
			closeDbResources(conn, pstmt);
		}
		return phoneNumbers;
	}

	/**
	 * Fetch birthdate profiles to which we can send email in current batch.
	 *
	 * @return the list
	 */
	public static List<UserProfile> fetchBirthdateProfiles() {

		/**Select the profiles whose registered birthdate is today
		 * check if the registered prior period lies within current batch or already past the time
		 * If they do, check if email is already sent today
		 */
		String SQL = "SELECT * from contactsProfile "
				+ "where DATE_FORMAT(birthdate, '%m-%d') = DATE_FORMAT(CURDATE(), '%m-%d') "
				+ "and DATE_FORMAT(DATE_SUB(birthdate, INTERVAL wishpriorhours HOUR), '%Y-%m-%d %H:%i') < DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i')"
				+ "and DATE_FORMAT(lastswishtime,'%Y-%m-%d')  !=DATE_FORMAT(CURDATE(), '%Y-%m-%d')";

		Connection conn = createConnection();
		PreparedStatement pstmt = null;

		List<UserProfile> profiles = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				UserProfile userprofile = new UserProfile();
				userprofile.setProfileid(rs.getInt("profileid"));
				userprofile.setOwnerid(rs.getInt("ownerid"));
				userprofile.setFirstName(rs.getString("first_name"));
				userprofile.setLastName(rs.getString("last_name"));
				userprofile.setEmail(rs.getString("email"));
				userprofile.setDob(rs.getDate("birthdate"));
				userprofile.setWishPrior(rs.getInt("wishpriorhours"));
				userprofile.setLastWishTime(rs.getTimestamp("lastwishtime"));

				Address address = new Address();
				address.setAddrressLine1(rs.getString("Address1"));
				address.setAddrressLine2(rs.getString("Address2"));
				address.setCity(rs.getString("City"));
				address.setState(rs.getString("State"));
				address.setCountry(rs.getString("Country"));

				profiles.add(userprofile);
			}

			rs.close();
		} catch (Exception e) {
			Logger.log("Exception occured while fetching birthday proile during current batch"+e);
		} finally {
			closeDbResources(conn, pstmt);
		}
		return profiles;
	}


	/**
	 * Closes the open db resources.
	 *
	 * @param conn the connection object
	 * @param pstmt the preparedstatement
	 */
	private static void closeDbResources(Connection conn, PreparedStatement pstmt) {
		try {
			if (pstmt != null)
				conn.close();
		} catch (SQLException se) {
			Logger.log("Exception occured while closing the connection" + se);
		}

		try {
			if (conn != null)
				conn.close();
		} catch (SQLException se) {
			Logger.log("Exception occured while closing the connection" + se);
		}
	}
}