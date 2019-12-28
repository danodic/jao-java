package com.danodic.jao.exceptions;

public class CannotFindJaoElementException extends Exception {

	private static final long serialVersionUID = 1352232242053091333L;
	private final String name;
	private final String type;

	public CannotFindJaoElementException(String name, String type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String toString() {
		return String.format("Could not find any JAO %s named as '%s'", this.type, this.name);
	}

	@Override
	public String getMessage() {
		return toString();
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
}
