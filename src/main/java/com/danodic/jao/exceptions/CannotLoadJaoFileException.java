package com.danodic.jao.exceptions;

public class CannotLoadJaoFileException extends Exception {

	private static final long serialVersionUID = 1352232242053091333L;
	private String fileName;
	private Exception exception;

	public CannotLoadJaoFileException(String fileName, Exception e) {
		this.fileName = fileName;
		this.exception = e;
	}

	@Override
	public String toString() {
		return String.format("Could not load JAO file located at '%s'. Reason:\n%s", this.fileName,
				this.exception.getMessage());
	}
	
	@Override
	public String getMessage() {
		return toString();
	}
}
