package com.danodic.jao.exceptions;

public class InvalidJaoParameterNameException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String parameterName;

    public InvalidJaoParameterNameException(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public String toString() {
        return String.format("Could not find a parameter named as %s at the layer.", parameterName);
    }

    @Override
    public String getMessage() {
        return toString();
    }

}