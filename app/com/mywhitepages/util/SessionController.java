package com.mywhitepages.util;

import play.mvc.Scope.Session;

public class SessionController {

	public static boolean isLoggedIn(Session session){
		boolean isLoggedIn = false;
		if(session!=null){
			if(session.get("id")!=null){
				isLoggedIn = true;
			}
		}
		return isLoggedIn;
	}

	public static void logOut(Session session) {
		if(session!=null){
			session.remove("id");
		}	
	}
}
