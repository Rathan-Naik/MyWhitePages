package controllers;

import play.*;
import play.db.DB;
import play.mvc.*;
import play.mvc.Scope.Session;

import java.util.*;

import com.mywhitepages.storagemanager.DBConnection;
import com.mywhitepages.storagemanager.UserRegisterdException;
import com.mywhitepages.util.MD5Util;

import models.User;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;

public class RegistrationController extends Controller {

	static User user;

	public static void register() {
		String fname = params.get("fname");
		String lName = params.get("lname");
		String password = params.get("psw");
		String email = params.get("email").trim();	

		password = MD5Util.getHashValue(password);
		user = new User(fname,lName,email,password);

		try {
			DBConnection.registerUser(user);
			Logger.info(request.remoteAddress+" has created a new account with email-"+email);

		} catch (UserRegisterdException e) {
			renderArgs.put("ErrorInfo", " Email already Registered");
			render("Registration/register.html");
		}
		
		user = DBConnection.getUser(email);
		
		if(session==null){
			session = new  Session();
			
		}
		
		session.put("id", user.getUserId());
		
		renderArgs.put("User", user);
		render("Registration/registered.html");

	}

}