package com.danodic.jao.exceptions;

public class UnknownJaoFileFormatExcepton extends Exception {

	private static final long serialVersionUID = 1352232242053091333L;
	private String fileName;

	public UnknownJaoFileFormatExcepton(String fileName) {
		this.fileName = fileName;
	}

	public String toString() {
		return String.format("Unkown file format: '%s'.", this.fileName);
	}

	public String getMessage() {
		return toString();
	}
}
