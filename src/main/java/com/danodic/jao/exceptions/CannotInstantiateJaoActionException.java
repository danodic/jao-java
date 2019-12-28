package com.danodic.jao.exceptions;

public class CannotInstantiateJaoActionException extends Exception {
	private static final long serialVersionUID = -3157203023738787038L;

	private String eventName;
	private Exception exception;

	public CannotInstantiateJaoActionException(String eventName, Exception e) {
		this.eventName = eventName;
		this.exception = e;
	}

	@Override
	public String toString() {
		return String.format("Could not instantiate the event '%s'. Reason:\n%s", this.eventName,
				this.exception.getMessage());
	}
	
	@Override
	public String getMessage() {
		return toString();
	}
}
