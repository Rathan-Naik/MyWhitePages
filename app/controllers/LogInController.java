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


/**
 * The Class LogInController is the controller for the user login flow
 */
public class LogInController extends Controller {

	public static void login(){
		User user;
		
		/**
		 *  Should not happen, but still can be simulated from tools like JMeter 
		 */
		if(params==null){
			render("main.html");
		}

		String password = params.get("psw");
		String email = params.get("email");	

		user = DBConnection.getUser(email);
		if(user==null ){
			renderArgs.put("msg","Invalid Credentials");
			render("main.html");
		}else {
			if( MD5Util.doPasswordsMatch(password, user.getPassword())){
				session.put("id", user.getUserId());
				Logger.info(request.remoteAddress+" has sucessfully loggedin with email-"+email);

			}
		}

		session.put("id", user.getUserId());
		Logger.info("Successfully logged in user-"+user.getUserId());

		renderArgs.put("User", user);
		render("Registration/registered.html");

	}

}