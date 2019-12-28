package com.danodic.jao.exceptions;

public class CannotInstantiateJaoRenderer extends Exception {
	private static final long serialVersionUID = -3157203023738787038L;

	private Exception exception;

	public CannotInstantiateJaoRenderer(Exception e) {
		this.exception = e;
	}

	@Override
	public String toString() {
		return String.format("Could not instantiate the renderer class. Reason:\n%s", this.exception.getMessage());
	}
	
	@Override
	public String getMessage() {
		return toString();
	}
}
