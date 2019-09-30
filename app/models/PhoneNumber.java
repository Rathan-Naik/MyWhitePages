package models;

public class PhoneNumber {
	
	int profileId;
	String phoneNumber;
	String category;
	
	public PhoneNumber() {
	}

	public PhoneNumber(int profileId, String phoneNumber, String category) {
		super();
		this.profileId = profileId;
		this.phoneNumber = phoneNumber;
		this.category = category;
	}
	
	public int getProfileId() {
		return profileId;
	}

	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}


}
