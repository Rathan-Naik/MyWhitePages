package notifiers;
 
import play.*;
import play.mvc.*;
import java.util.*;

import org.apache.commons.mail.EmailAttachment;

import models.User;
import models.UserProfile;
 
public class Emailer extends Mailer {
 
   public static void sendWishes(UserProfile user, User owner) {
	   
      setSubject("Happy Birthday to %s", user.getFirstName());
      
      addRecipient(user.getEmail().trim());
      setFrom(owner.getEmail());
     
      send(user, owner);
   }
 
}