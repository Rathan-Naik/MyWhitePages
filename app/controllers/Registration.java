package controllers;

import play.*;
import play.db.DB;
import play.mvc.*;
import play.mvc.Scope.Session;
import util.MD5Util;

import java.util.*;

import java.sql.Statement;

import db.DBConnection;
import db.UserRegisterdException;

import java.sql.Connection;
import java.sql.SQLException;

import models.*;

public class Registration extends Controller {

	static User user;

	public static void register() throws SQLException{
		String fname = params.get("fname");
		String lName = params.get("lname");
		String password = params.get("psw");
		String email = params.get("email").trim();	

		password = MD5Util.getHashValue(password);
		user = new User(fname,lName,email,password);

		try {
			DBConnection.registerUser(user);
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