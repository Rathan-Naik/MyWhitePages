package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import com.mywhitepages.util.SessionController;


public class Application extends Controller {

    public static void index() {
    	Logger.info(request.remoteAddress+" has visited home page");
        render();
    }
    
    public static void register(){
    	render("Registration/register.html");
    }
    
    public static void create(){
		if(!SessionController.isLoggedIn(session)){
			renderArgs.put("msg", "Please Login");
			render("main.html");
		}
    	render("CreateContact/createcontact.html");
    }
    
    public static void viewContacts(){
		if(!SessionController.isLoggedIn(session)){
			renderArgs.put("msg", "Please Login");
			render("main.html");
		}
    	render("ViewContacts/viewContacts.html");
    }
    
    public static void homePage(){
		if(!SessionController.isLoggedIn(session)){
			renderArgs.put("msg", "Please Login");
			render("main.html");
		}
    	render("Registration/registered.html");
    }
    
    public static void logOut(){
    	SessionController.logOut(session);
    	render("main.html");
    }
    
    
    

}