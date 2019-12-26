package com.danodic.jao.exceptions;

public class CannotFindJaoActionException extends CannotFindJaoElementException {

	private static final long serialVersionUID = -6910418402247960733L;

	public CannotFindJaoActionException(String name) {
		super(name, "action");
	}

}
