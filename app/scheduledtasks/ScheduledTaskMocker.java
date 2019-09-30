package scheduledtasks;
import java.util.List;

import db.DBConnection;
import models.User;
import models.UserProfile;
import notifiers.Emailer;
import play.jobs.*;

public class ScheduledTaskMocker extends Job {

	public void doJob() {
		UserProfile user = new UserProfile();
		user.setFirstName("Mark");
		user.setLastName("Taylor");
		user.setEmail("guestuser.email3@gmail.com");

		User owner = new User();
		owner.setFirstName("Rathan");
		owner.setLastName("Naik");
		owner.setEmail("guestuser.email3@gmail.com");

		Emailer.sendWishes(user,owner);


	}
}