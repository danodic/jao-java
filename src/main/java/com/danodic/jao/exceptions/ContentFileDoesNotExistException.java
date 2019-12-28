package com.danodic.jao.exceptions;

public class ContentFileDoesNotExistException extends Exception {

	private static final long serialVersionUID = 1352232242053091333L;
	private String fileName;

	public ContentFileDoesNotExistException(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return String.format("The path provided for the JAO file at '%s' is invalid.", this.fileName);
	}

	@Override
	public String getMessage() {
		return toString();
	}
}
