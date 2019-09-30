package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.jamonapi.utils.Logger;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import models.Address;
import models.PhoneNumber;
import models.User;
import models.UserProfile;

public class DBConnection {
	public static Connection createConnection()
	{
		Connection con = null;
		String url = "jdbc:mysql://localhost/mywhitepages?characterEncoding=latin1"; 
		String username = "mywhitepagesapp"; //MySQL username
		String password = "mywhitepagesapp"; //MySQL password

		try 
		{
			try 
			{
				Class.forName("com.mysql.jdbc.Driver"); //loading mysql driver 
			} 
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			} 
			con = DriverManager.getConnection(url, username, password); //attempting to connect to MySQL database
			System.out.println("Printing connection object "+con);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return con; 
	}


	public static void registerUser(User user) throws UserRegisterdException{

		String SQL = "INSERT INTO Users (first_name,last_name,email,password) "
				+ "VALUES(?,?,?,?)";
		long id;
		try {
			Connection conn = createConnection();
			PreparedStatement pstmt = conn.prepareStatement(SQL,
					Statement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, user.getFirstName());
			pstmt.setString(2, user.getLastName());
			pstmt.setString(3, user.getEmail());
			pstmt.setString(4, user.getPassword());

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {

				try (ResultSet rs = pstmt.getGeneratedKeys()) {
					if (rs.next()) {
						id = rs.getLong(1);
					}
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		} catch (MySQLIntegrityConstraintViolationException me){
			throw new  UserRegisterdException();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} 
	}


	public static User getUser(String email){

		User user = null;

		String SQL = "SELECT * from users where email=?";
		Connection conn = createConnection();
		PreparedStatement pstmt =null;

		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, email);

			ResultSet rs = pstmt.executeQuery();

			rs.next();
			user = new User(rs.getInt("userid"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("password"));


			rs.close();
		}catch(SQLException se){
			Logger.log(se);

		}catch(Exception e){

		}finally{

			try{
				if(pstmt!=null)
					conn.close();
			}catch(SQLException se){
			}

			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		return user;
	}
	
	public static User getUser(int userid){

		User user = null;

		String SQL = "SELECT * from users where userid=?";
		Connection conn = createConnection();
		PreparedStatement pstmt =null;

		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, userid);

			ResultSet rs = pstmt.executeQuery();

			rs.next();
			user = new User(rs.getInt("userid"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("password"));


			rs.close();
		}catch(SQLException se){
			Logger.log(se);

		}catch(Exception e){

		}finally{

			try{
				if(pstmt!=null)
					conn.close();
			}catch(SQLException se){
			}

			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		return user;
	}


	public static void createContactProfile(int userid, UserProfile userProfile) throws UserRegisterdException{


		String SQL = "INSERT INTO contactsProfile (ownerid,first_name,last_name,email,Address1,Address2,City,State,Country,image,birthdate,wishpriorhours,lastwishtime) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ";


		Connection conn = createConnection();
		PreparedStatement pstmt =null;

		int i=1;
		try {
			pstmt = conn.prepareStatement(SQL);

			pstmt.setInt(i++,userid);
			pstmt.setString(i++, userProfile.getFirstName());
			pstmt.setString(i++, userProfile.getLastName());
			pstmt.setString(i++, userProfile.getEmail());

			Address address = userProfile.getAddress();
			pstmt.setString(i++, address.getAddrressLine1());
			pstmt.setString(i++, address.getAddrressLine2());
			pstmt.setString(i++, address.getCity());
			pstmt.setString(i++, address.getState());
			pstmt.setString(i++, address.getCountry());
			pstmt.setString(i++,null);

			pstmt.setDate (i++,userProfile.getDob());
			pstmt.setInt(i++, userProfile.getWishPrior());
			pstmt.setDate(i++, null);			


			boolean success = pstmt.execute();

		} catch (MySQLIntegrityConstraintViolationException me){
			throw new  UserRegisterdException();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static void insertPhoneNumber (List<PhoneNumber> numbers) throws UserRegisterdException{


		String SQL = "INSERT INTO PhoneNumbers (profileid,phonenumber,Category) VALUES (?,?,?)";
		String values =  ",(?,?,?) ";

		try
		{
			Connection conn = createConnection();
			PreparedStatement pstmt =null;

			for(int i=1;i< numbers.size(); i++){
				SQL += values ;
			}

			pstmt = conn.prepareStatement(SQL);

			int i=1;
			for(PhoneNumber phNo : numbers){
				pstmt.setInt(i++, phNo.getProfileId());
				pstmt.setString(i++, phNo.getPhoneNumber());
				pstmt.setString(i++, phNo.getCategory());
			}

			boolean sucess = pstmt.execute();

		} catch (MySQLIntegrityConstraintViolationException me){
			throw new  UserRegisterdException();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static void addPhoneNumber(UserProfile userProfile) throws UserRegisterdException{


		String SQL = "INSERT INTO contactsProfile (ownerid,first_name,last_name,email,Address1,Address2,City,State,Country,image,birthdate,wishpriorhours,lastwishtime) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ";


		Connection conn = createConnection();
		PreparedStatement pstmt =null;

		int i=1;
		try {
			pstmt = conn.prepareStatement(SQL);

			pstmt.setInt(i++,1);
			pstmt.setString(i++, userProfile.getFirstName());
			pstmt.setString(i++, userProfile.getLastName());
			pstmt.setString(i++, userProfile.getEmail());

			Address address = userProfile.getAddress();
			pstmt.setString(i++, address.getAddrressLine1());
			pstmt.setString(i++, address.getAddrressLine2());
			pstmt.setString(i++, address.getCity());
			pstmt.setString(i++, address.getState());
			pstmt.setString(i++, address.getCountry());
			pstmt.setString(i++,null);

			pstmt.setDate (i++,userProfile.getDob());
			pstmt.setInt(i++, userProfile.getWishPrior());
			pstmt.setDate(i++, null);			


			boolean success = pstmt.execute();

		} catch (MySQLIntegrityConstraintViolationException me){
			throw new  UserRegisterdException();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} 
	}


	public static List<UserProfile> viewContactProfile(int userid, String orderby, String order, int limit ) {

		String SQL = "SELECT * from contactsProfile where ownerid=? ";
		if(orderby!=null){
			SQL = SQL +"order by "+orderby+" "+ order;
		}

		if(limit > 0){
			SQL = SQL +" limit "+limit;
		}

		Connection conn = createConnection();
		PreparedStatement pstmt =null;

		List<UserProfile> profiles = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, userid);

			ResultSet rs = pstmt.executeQuery();

			while(rs.next()){
				UserProfile userprofile = new UserProfile();
				userprofile.setProfileid(rs.getInt("profileid"));
				userprofile.setOwnerid(rs.getInt("ownerid"));
				userprofile.setFirstName(rs.getString("first_name"));
				userprofile.setLastName(rs.getString("last_name"));
				userprofile.setEmail(	rs.getString("email"));
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
		}catch(SQLException se){
			Logger.log(se);

		}catch(Exception e){

		}finally{

			try{
				if(pstmt!=null)
					conn.close();
			}catch(SQLException se){
			}

			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		return profiles;
	}


	public static List<UserProfile> viewContactProfiles(int currentUserId, String orderby, String order) {
		return viewContactProfile(currentUserId,orderby,order,0);
	}

	public static UserProfile fetchProfile(int profileid) {

		String SQL = "SELECT * from contactsProfile where profileid=? ";

		Connection conn = createConnection();
		PreparedStatement pstmt =null;

		UserProfile profile = null;
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, profileid);

			ResultSet rs = pstmt.executeQuery();

			if(rs.next()){

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
		}catch(SQLException se){
			Logger.log(se);

		}catch(Exception e){

		}finally{

			try{
				if(pstmt!=null)
					conn.close();
			}catch(SQLException se){
			}

			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		return profile;
	}

	public static List<PhoneNumber> fetchPhoneNumbers(int profileid ) {

		String SQL = "SELECT * from phonenumbers where profileid=? ";


		Connection conn = createConnection();
		PreparedStatement pstmt =null;

		List<PhoneNumber> phoneNumbers = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, profileid);

			ResultSet rs = pstmt.executeQuery();

			while(rs.next()){
				PhoneNumber phNo = new PhoneNumber();
				phNo.setPhoneNumber(rs.getString("phonenumber"));
				phNo.setCategory(rs.getString("Category"));
				phNo.setPhoneNumber(rs.getString("phonenumber"));
				phNo.setProfileId(rs.getInt("profileid"));

				phoneNumbers.add(phNo);	
			}

			rs.close();
		}catch(SQLException se){
			Logger.log(se);

		}catch(Exception e){

		}finally{

			try{
				if(pstmt!=null)
					conn.close();
			}catch(SQLException se){
			}

			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		return phoneNumbers;
	}


	public static List<UserProfile> fetchBirthdateProfiles() {

		String SQL = "SELECT * from contactsProfile "+
				"where DATE_FORMAT(birthdate, '%m-%d') = DATE_FORMAT(CURDATE(), '%m-%d') "+
				"and DATE_FORMAT(DATE_SUB(birthdate, INTERVAL wishpriorhours HOUR), '%Y-%m-%d %H:%i') < DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i')"+
				"and DATE_FORMAT(lastswishtime,'%m-%d')  !=DATE_FORMAT(CURDATE(), '%m-%d')";

		Connection conn = createConnection();
		PreparedStatement pstmt =null;

		List<UserProfile> profiles = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next()){
				UserProfile userprofile = new UserProfile();
				userprofile.setProfileid(rs.getInt("profileid"));
				userprofile.setOwnerid(rs.getInt("ownerid"));
				userprofile.setFirstName(rs.getString("first_name"));
				userprofile.setLastName(rs.getString("last_name"));
				userprofile.setEmail(	rs.getString("email"));
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
		}catch(SQLException se){
			Logger.log(se);

		}catch(Exception e){

		}finally{

			try{
				if(pstmt!=null)
					conn.close();
			}catch(SQLException se){
			}

			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		return profiles;
	}
}