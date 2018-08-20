package com.briup.environment.bean;

public class UserBean {
	private int id;
	private String uname;
	private String upass;
	private int type;
	public UserBean() {
		
	}
	public UserBean(int id, String uname, String upass,int type) {
		this.id = id;
		this.uname = uname;
		this.upass = upass;
		this.type=type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUpass() {
		return upass;
	}
	public void setUpass(String upass) {
		this.upass = upass;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	

}
