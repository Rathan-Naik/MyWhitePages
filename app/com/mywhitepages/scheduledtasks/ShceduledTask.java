package com.mywhitepages.scheduledtasks;
import java.util.List;

import com.mywhitepages.notifiers.Emailer;
import com.mywhitepages.storagemanager.DBConnection;

import models.User;
import models.UserProfile;
import play.jobs.*;

@Every("1h")
public class ShceduledTask extends Job {

	public void doJob() {
		List<UserProfile> users = DBConnection.fetchBirthdateProfiles();

		for(UserProfile user: users){
			if(user.getEmail()!=null){

				User owner = DBConnection.getUser(user.getOwnerid());
				Emailer.sendWishes(user,owner);
			}
		}
	}
	
}