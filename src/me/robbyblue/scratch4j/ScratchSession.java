package me.robbyblue.scratch4j;

public class ScratchSession {

	private String username;
	private String sessionid;
	private String csrftoken;

	public ScratchSession(String username, String sessionid, String csrftoken) {
		this.username = username;
		this.sessionid = sessionid;
		this.csrftoken = csrftoken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getCsrftoken() {
		return csrftoken;
	}

	public void setCsrftoken(String csrftoken) {
		this.csrftoken = csrftoken;
	}

}
