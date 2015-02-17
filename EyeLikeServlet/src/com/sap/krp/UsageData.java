package com.sap.krp;

import java.util.Random;

public class UsageData {

	private String value;
	private static UsageData instance = null;
	private int maxParticipants;

	public static synchronized UsageData getInstance() {
		if (instance == null) {
			instance = new UsageData();
		}
		return instance;
	}
	
	private UsageData() {
		maxParticipants = 0;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		Integer currentValue = Integer.valueOf(value);
		maxParticipants = maxParticipants >= currentValue ? maxParticipants : currentValue; 
	}

	private String toJSON(String val) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append(String.format(" \"value\": \"%s\" ", val));
		sb.append("}");
		return sb.toString();
	}
	
	public String toJSON() {
		int currentVal = (int) ((Float.valueOf(value) / (float)maxParticipants) * 100);
		return this.toJSON(String.valueOf(currentVal));
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
