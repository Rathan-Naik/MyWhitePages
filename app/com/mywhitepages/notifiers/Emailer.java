package com.mywhitepages.notifiers;
 
import play.*;
import play.mvc.*;
import java.util.*;

import org.apache.commons.mail.EmailAttachment;

import com.mywhitepages.models.EmailUser;
import com.mywhitepages.models.User;
import com.mywhitepages.models.UserProfile;
 
public class Emailer extends Mailer {
 
   public static void sendWishes(UserProfile user, User owner) {
	  
	  EmailUser emailObj = new EmailUser();
	  emailObj.setRecvFirstName(user.getFirstName());
	  emailObj.setRecvLastName(user.getLastName());
	   
	  emailObj.setSenderFirstName(owner.getFirstName());
	  emailObj.setSenderLastName(owner.getLastName());
	   
	  
      setSubject("Happy Birthday to %s", user.getFirstName());
      
      addRecipient(user.getEmail().trim());
      setFrom(owner.getEmail().trim());
     
      send(emailObj);
   }
}