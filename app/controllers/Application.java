package controllers;

import play.*;
import play.mvc.*;
import util.SessionController;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void sayHello(String name){
    	render("Application/a.html");
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