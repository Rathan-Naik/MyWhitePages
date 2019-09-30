package com.mywhitepages.models;

public class EmailUser {

	private String recvFirstName;
	private String recvLastName;

	private String senderLastName;
	private String senderFirstName;
	
	public String getRecvFirstName() {
		return recvFirstName;
	}
	public void setRecvFirstName(String recvFirstName) {
		this.recvFirstName = recvFirstName;
	}
	public String getRecvLastName() {
		return recvLastName;
	}
	public void setRecvLastName(String recvLastName) {
		this.recvLastName = recvLastName;
	}
	public String getSenderLastName() {
		return senderLastName;
	}
	public void setSenderLastName(String senderLastName) {
		this.senderLastName = senderLastName;
	}
	public String getSenderFirstName() {
		return senderFirstName;
	}
	public void setSenderFirstName(String senderFirstName) {
		this.senderFirstName = senderFirstName;
	}

	
	
}