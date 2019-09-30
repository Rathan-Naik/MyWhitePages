package models;

import java.sql.Date;
import java.sql.Timestamp;

public class UserProfile {

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public int getWishPrior() {
		return wishPrior;
	}

	public void setWishPrior(int wishPrior) {
		this.wishPrior = wishPrior;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Timestamp getLastWishTime() {
		return lastWishTime;
	}

	public void setLastWishTime(Timestamp lastWishTime) {
		this.lastWishTime = lastWishTime;
	}


	public int getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(int ownerid) {
		this.ownerid = ownerid;
	}

	
	private String firstName; 
	private String lastName; 
	private String email;
	private Date dob ; 
	private int wishPrior;
	private Timestamp lastWishTime;
	private int ownerid;
	private Address address;



	private int profileid;
	
	
	public UserProfile(String firstName, String lastName, String email, Date dob, int wishPrior, Address address) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.dob = dob;
		this.wishPrior = wishPrior;
		this.address = address;
		
	}
	
	public UserProfile(){
		
	}

	public void setProfileid(int int1) {
		this.profileid = int1;
	}
	public int getProfileid() {
		return profileid;
	}

}
