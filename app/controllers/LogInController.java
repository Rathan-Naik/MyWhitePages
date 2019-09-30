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

public class LogInController extends Controller {

	static User user;

	public static void login() throws SQLException{
		

		String password = params.get("psw");
		String email = params.get("email");	

		user = DBConnection.getUser(email);
		if(user==null){
			renderArgs.put("msg","Invalid Credentials");
			render("main.html");
		}else {
			if( MD5Util.doPasswordsMatch(password, user.getPassword())){
				session.put("id", user.getUserId());
			}
		}
		
		
		session.put("id", user.getUserId());
		Logger.info("");


		renderArgs.put("User", user);
		render("Registration/registered.html");

	}

}