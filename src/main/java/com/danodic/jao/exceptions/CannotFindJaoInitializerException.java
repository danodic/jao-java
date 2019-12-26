package com.danodic.jao.exceptions;

public class CannotFindJaoInitializerException extends CannotFindJaoElementException {

	private static final long serialVersionUID = -6910418402247960733L;

	public CannotFindJaoInitializerException(String name) {
		super(name, "initializer");
	}

}
