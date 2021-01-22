package com.danodic.jao.exceptions;

public class JaoCloneException extends RuntimeException {

    private static final long serialVersionUID = -3157203023738787038L;

    private final Exception exception;

    public JaoCloneException(Exception e) {
        this.exception = e;
    }

    @Override
    public String toString() {
        return String.format("Could not clone a Jao element. Reason:%n%s", this.exception.getMessage());
    }

    @Override
    public String getMessage() {
        return toString();
    }
}
