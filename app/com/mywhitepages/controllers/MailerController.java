package com.mywhitepages.controllers;

import com.mywhitepages.notifiers.Emailer;
import com.mywhitepages.scheduledtasks.ScheduledTaskMocker;
import com.mywhitepages.scheduledtasks.ShceduledTask;

import play.Logger;
import play.mvc.Controller;


public class MailerController extends Controller { 

	public static void sendMail(){
		new ScheduledTaskMocker().now();
		Logger.info("Email sent");
		render("Emailer/mocker.html");
	}
}
