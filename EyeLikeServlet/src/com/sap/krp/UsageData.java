package com.sap.krp;

public class UsageData {

	private String value;

	public UsageData(String value) {
		this.value = value;
	}

	public UsageData() {
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append(String.format(" \"value\": \"%s\" ", value));
		sb.append("}");
		return sb.toString();
	}

}
