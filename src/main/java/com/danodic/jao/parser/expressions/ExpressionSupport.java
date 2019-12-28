package com.danodic.jao.parser.expressions;

/**
 * Some support functions to be used in the expression parser.
 * 
 * @author danodic
 *
 */
public class ExpressionSupport {

	private ExpressionSupport() {
		
	}

	/**
	 * Will return the representation in long for a given amount of seconds in float
	 * format.
	 * 
	 * @param seconds Amount of seconds in float format.
	 * @return Amount of milliseconds converted from the seconds in float format.
	 */
	public static long seconds(float seconds) {
		return (long) (seconds * 1000f);
	}

}
