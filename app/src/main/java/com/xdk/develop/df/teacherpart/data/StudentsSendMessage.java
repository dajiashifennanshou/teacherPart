package com.xdk.develop.df.teacherpart.data;

import java.io.Serializable;
import java.util.ArrayList;

public class StudentsSendMessage implements Serializable{
	private ArrayList<CurrentUser> users;
	private String issuer, issuedate, motif, content, validdate;
	public ArrayList<CurrentUser> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<CurrentUser> users) {
		this.users = users;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getIssuedate() {
		return issuedate;
	}
	public void setIssuedate(String issuedate) {
		this.issuedate = issuedate;
	}
	public String getMotif() {
		return motif;
	}
	public void setMotif(String motif) {
		this.motif = motif;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getValiddate() {
		return validdate;
	}
	public void setValiddate(String validdate) {
		this.validdate = validdate;
	}
	
}
