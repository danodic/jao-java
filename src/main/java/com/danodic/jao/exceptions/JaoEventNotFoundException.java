package com.danodic.jao.exceptions;

public class JaoEventNotFoundException extends Exception {

	private static final long serialVersionUID = 1352232242053091333L;
	private final String eventName;

	public JaoEventNotFoundException(String eventName) {
		this.eventName = eventName;
	}

	@Override
	public String toString() {
		return String.format("Could not find event named as %s inside layer.", this.eventName);
	}

	@Override
	public String getMessage() {
		return toString();
	}
}
