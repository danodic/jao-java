package com.danodic.jao.exceptions;

public class CannotLoadJaoFileContentException extends Exception {

	private static final long serialVersionUID = 1352232242053091333L;
	private String fileName;
	private Exception exception;

	public CannotLoadJaoFileContentException(String fileName, Exception e) {
		this.fileName = fileName;
		this.exception = e;
	}

	public String toString() {
		return String.format("Could not load JAO dependency file at '%s'. Reason:\n%s", this.fileName,
				this.exception.getMessage());
	}
	
	public String getMessage() {
		return toString();
	}
}
