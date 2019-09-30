package controllers;

import play.*;
import play.db.DB;
import play.mvc.*;
import play.mvc.Scope.Session;
import util.SessionController;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DBConnection;
import db.UserRegisterdException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import models.*;

public class ContactsController extends Controller {

	static User user;

	public static void createContact() throws SQLException{

		if(!SessionController.isLoggedIn(session)){
			renderArgs.put("ErrorInfo", "Please Login");
			render("main.html");
		}
		
		int userId = Integer.parseInt(session.get("id"));

		String fname = params.get("fname");
		String lName = params.get("lname");
		String email = params.get("email");	
		Date dob = Date.valueOf(params.get("dob")); 

		int wishPrior = Integer.parseInt(params.get("wish"));

		String addr1 = params.get("addr1");
		String addr2 = params.get("addr2");

		String city = params.get("city");
		String state = params.get("state");
		String country = params.get("country");	

		String[] phoneNumber = params.getAll("phNo");
		String[] category =  params.getAll("catg");

		Address address = new Address(addr1, addr2, city, state, country);
		UserProfile userProfile = new UserProfile(fname, lName, email, dob, wishPrior, address);

		try {
			DBConnection.createContactProfile(userId, userProfile);
		} catch (UserRegisterdException e1) {
			e1.printStackTrace();
		}

		int currentUserId = Integer.parseInt(session.get("id"));
		try {
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

		} catch (UserRegisterdException e1) {
			e1.printStackTrace();
		}
		
		renderArgs.put("msg", "Contact Successfully Created");
		render("Registration/registered.html");

	}

	public static void viewContacts() throws SQLException{

		if(!SessionController.isLoggedIn(session)){
			renderArgs.put("ErrorInfo", "Please Login");
			render("main.html");
		}

		int currentUserId = Integer.parseInt(session.get("id"));
		List<UserProfile> profiles = DBConnection.viewContactProfiles(currentUserId, "first_name", "asc");

		renderArgs.put("profiles", profiles);
		render("ViewContacts/viewcontacts.html");

	}
	
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


}