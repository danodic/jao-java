package com.danodic.jao.exceptions;

public class CannotInstantiateJaoActiontException extends Exception {
	private static final long serialVersionUID = -3157203023738787038L;

	private String eventName;
	private Exception exception;

	public CannotInstantiateJaoActiontException(String eventName, Exception e) {
		this.eventName = eventName;
		this.exception = e;
	}

	public String toString() {
		return String.format("Could not instantiate the event '%s'. Reason:\n%s", this.eventName,
				this.exception.getMessage());
	}
	
	public String getMessage() {
		return toString();
	}
}
