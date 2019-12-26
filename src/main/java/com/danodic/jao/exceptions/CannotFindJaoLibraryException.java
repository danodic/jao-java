package com.danodic.jao.exceptions;

public class CannotFindJaoLibraryException extends CannotFindJaoElementException {

	private static final long serialVersionUID = -6910418402247960733L;

	public CannotFindJaoLibraryException(String name) {
		super(name, "library");
	}

}
