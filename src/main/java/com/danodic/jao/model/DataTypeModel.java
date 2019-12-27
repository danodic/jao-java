package com.danodic.jao.model;

import java.util.Map;

public class DataTypeModel {

	private String type;
	private Map<String, String> attributes;

	public String getType() {
		return type;
	}

	public void setType(String name) {
		this.type = name;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
