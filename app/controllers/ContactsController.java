package controllers;

import play.*;
import play.db.DB;
import play.mvc.*;
import play.mvc.Scope.Session;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mywhitepages.storagemanager.DBConnection;
import com.mywhitepages.storagemanager.UserRegisterdException;
import com.mywhitepages.util.SessionController;

import models.Address;
import models.PhoneNumber;
import models.UserProfile;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;


/**
 * The Class ContactsController handles all contacts related activities.
 */
public class ContactsController extends Controller {

	/**
	 * Creates the contact.
	 *
	 */
	public static void createContact() {

		if(!SessionController.isLoggedIn(session)){
			renderArgs.put("ErrorInfo", "Please Login");
			render("main.html");
		}

		/** Parameters cannot be null
		 *  If they are not a valid submission, 
		 *  lets ask the user to fill the form
		 *  
		 *  Also possible when simulated from tools like JMeter, avoid such users
		 */
		if(params==null){
			render("CreateContact/createcontact.html");
		}

		int userId = Integer.parseInt(session.get("id"));

		String fname = params.get("fname");
		String lName = params.get("lname");
		String email = params.get("email");	
		
		Date dob = null;
		try{
			Date.valueOf(params.get("dob")); 
		} catch(Exception e){
			Logger.info("Illegeal DOB provided");
		}

		int wishPrior=0;
		if(params.get("wish")!=null && !params.get("wish").trim().isEmpty())
			wishPrior = Integer.parseInt(params.get("wish"));

		String addr1 = params.get("addr1");
		String addr2 = params.get("addr2");
		String city = params.get("city");
		String state = params.get("state");
		String country = params.get("country");	

		String[] phoneNumber = params.getAll("phNo");
		String[] category =  params.getAll("catg");

		Address address = new Address(addr1, addr2, city, state, country);
		UserProfile userProfile = new UserProfile(fname, lName, email, dob, wishPrior, address);


		DBConnection.createContactProfile(userId, userProfile);

		int currentUserId = Integer.parseInt(session.get("id"));
		
		insertPhoneNumbers(phoneNumber, category, currentUserId);
		renderArgs.put("msg", "Contact Successfully Created");
		render("Registration/registered.html");

	}

	/**
	 * Insert phone numbers.
	 *
	 * @param phoneNumber the phone number
	 * @param category the category
	 * @param currentUserId the current user id
	 */
	private static void insertPhoneNumbers(String[] phoneNumber, String[] category, int currentUserId) {
		/**
		 * Since we have created a Contact profile now, fetch the auto-incremented profileid 
		 * Fetch the latest profile created and get id
		 * Profile id required to link with phone numbers 
		 */
		List<UserProfile> profile = DBConnection.viewContactProfile(currentUserId, "createdate", "desc", 1);
		int profileId = profile.get(0).getProfileid();

		List<PhoneNumber> phNoList = new ArrayList<>();
		for(int i=0;i<phoneNumber.length;i++){
			String categ=null;
			if(i<category.length){
				categ = category[i];
			}
			if(phoneNumber[i]!=null){
				PhoneNumber phNumber = new PhoneNumber(profileId, phoneNumber[i], categ);
				phNoList.add(phNumber);
			}
		}

		DBConnection.insertPhoneNumber(phNoList);
	}

	/**
	 * View all contacts created by this user
	 *
	 * @throws SQLException the SQL exception
	 */
	public static void viewContacts() throws SQLException{

		if(!SessionController.isLoggedIn(session)){
			renderArgs.put("ErrorInfo", "Please Login");
			render("main.html");
		}

		int currentUserId = Integer.parseInt(session.get("id"));
		List<UserProfile> profiles = DBConnection.viewContactProfiles(currentUserId, "first_name", "asc");

		if(profiles.isEmpty()){
			renderArgs.put("profiles", 0);
		} else{
			renderArgs.put("profiles", profiles);
		}
		render("ViewContacts/viewcontacts.html");
	}

	/**
	 * View the specific profile details identified by profileid
	 *
	 * @throws SQLException the SQL exception
	 */
	public static void viewProfile() throws SQLException{

		if(!SessionController.isLoggedIn(session)){
			renderArgs.put("ErrorInfo", "Please Login");
			render("main.html");
		}
		int currentUserId = Integer.parseInt(session.get("id"));
		currentUserId=1;

		int profileId = Integer.parseInt(params.get("profileid"));
		UserProfile profile = DBConnection.fetchProfile(profileId) ;

		List<PhoneNumber> phNoList = DBConnection.fetchPhoneNumbers(profileId);

		List<String> phNoString = new ArrayList<>();
		List<String> category = new ArrayList<>();
		for(PhoneNumber ph: phNoList){
			phNoString.add( ph.getPhoneNumber());
			category.add(ph.getCategory());
		}


		renderArgs.put("profile", profile);
		renderArgs.put("phNoList", phNoString);
		renderArgs.put("category", category);


		render("ViewContacts/fetchprofile.html");

	}
	
	/**
	 * Delete the specific profile details identified by profileid
	 *
	 * @throws SQLException the SQL exception
	 */
	public static void deleteProfile(){

		if(!SessionController.isLoggedIn(session)){
			renderArgs.put("ErrorInfo", "Please Login");
			render("main.html");
		}
		int currentUserId = Integer.parseInt(session.get("id"));
		currentUserId=1;

		int profileId = Integer.parseInt(params.get("profileid"));
		DBConnection.deleteProfile(profileId);

		render("ViewContacts/deleteprofile.html");

	}


}