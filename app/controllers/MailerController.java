package controllers;

import notifiers.Emailer;
import play.Logger;
import play.mvc.Controller;
import scheduledtasks.ShceduledTask;


public class MailerController extends Controller { 

	public static void sendMail(){
		new ShceduledTask().now();
		
		Logger.info("sent");
	}
}
