package controllers;

import notifiers.Emailer;
import play.Logger;
import play.mvc.Controller;
import scheduledtasks.ScheduledTaskMocker;
import scheduledtasks.ShceduledTask;


public class MailerController extends Controller { 

	public static void sendMail(){
		new ScheduledTaskMocker().now();
		Logger.info("Email sent");
		render("Emailer/mocker.html");
	}
}
