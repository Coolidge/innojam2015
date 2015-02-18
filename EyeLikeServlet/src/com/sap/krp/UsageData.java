package com.sap.krp;

import java.util.Random;

public class UsageData {

	private String value;
	private static UsageData instance = null;

	public static synchronized UsageData getInstance() {
		if (instance == null) {
			instance = new UsageData();
		}
		return instance;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private String toJSON(String val) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append(String.format(" \"value\": \"%s\" ", val));
		sb.append("}");
		return sb.toString();
	}
	
	public String toJSON() {
		return this.toJSON(value);
	}
	
	public String toJsonRandomVal() {
		String number = randomNumber();
		return toJSON(number);
	}

	private String randomNumber() {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100);
		return String.valueOf(randomInt);
	}

}
