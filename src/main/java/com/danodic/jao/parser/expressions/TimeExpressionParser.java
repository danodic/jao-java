package com.danodic.jao.parser.expressions;


import java.util.Arrays;
import java.util.List;

import static com.danodic.jao.parser.expressions.ExpressionSupport.seconds;

public class TimeExpressionParser {

	public static Long parseExpression(String expression) {

		List<String> tokens;

		// Tokenize the expression
		tokens = tokenize(expression.toUpperCase());

		// Parse the expression
		switch (tokens.get(0)) {
		case "SECOND":
		case "SECONDS":
			return second(tokens.subList(1, tokens.size()));
		default:
			return 0l;
		}

	}

	private static List<String> tokenize(String expression) {
		return Arrays.asList(expression.split(" +"));
	}
	
	private static long second(List<String> tokens) {
		return seconds(Float.valueOf(tokens.get(0)));
	}

}
