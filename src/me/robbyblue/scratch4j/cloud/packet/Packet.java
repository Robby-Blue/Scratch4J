package me.robbyblue.scratch4j.cloud.packet;

import java.util.HashMap;

public class Packet {

	protected String method;
	protected HashMap<String, String> options = new HashMap<String, String>();

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public HashMap<String, String> getOptions() {
		return options;
	}

	public void setOptions(HashMap<String, String> options) {
		this.options = options;
	}

	public void addOption(String key, String value) {
		this.options.put(key, value);
	}

}
