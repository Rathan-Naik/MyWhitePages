package models;

public class Address {

	public String getAddrressLine1() {
		return addrressLine1;
	}

	public void setAddrressLine1(String addrressLine1) {
		this.addrressLine1 = addrressLine1;
	}

	public String getAddrressLine2() {
		return addrressLine2;
	}

	public void setAddrressLine2(String addrressLine2) {
		this.addrressLine2 = addrressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	String addrressLine1;
	String addrressLine2; 
	String city ; 
	String state; 
	String country; 
	
	public Address(String addrressLine1, String addrressLine2, String city, String state, String country) {
		this.addrressLine1 = addrressLine1;
		this.addrressLine2 = addrressLine2;
		this.city = city;
		this.state = state;
		this.country = country;
	}

	public Address() {
	}

}
