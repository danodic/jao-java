package com.danodic.jao.exceptions;

public class CannotFindJaoElementException extends Exception {

	private static final long serialVersionUID = 1352232242053091333L;
	private String name;
	private String type;

	public CannotFindJaoElementException(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String toString() {
		return String.format("Could not find any JAO %s named as '%s'", this.type, this.name);
	}

	public String getMessage() {
		return toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
