package com.danodic.jao.parser.expressions;

import static com.danodic.jao.parser.expressions.ExpressionSupport.seconds;

import java.util.Arrays;
import java.util.List;

public class TimeExpressionParser {

	private TimeExpressionParser() {

	}

	public static Long parseExpression(String expression) {

		List<String> tokens;

		// Tokenize the expression
		tokens = tokenize(expression.toUpperCase());

		// Parse the expression
		switch (tokens.get(0)) {
		case "SECOND":
		case "SECONDS":
			return second(tokens.subList(1, tokens.size()));
		case "BPM":
			return bpm(tokens.subList(1, tokens.size()));
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

	private static long bpm(List<String> tokens) {
		int step = bpmStep(tokens.subList(1, tokens.size()));
		double bpm = Double.parseDouble(tokens.get(0));
		return (long) ((60d / bpm) * 1000d * (double) step);
	}

	private static int bpmStep(List<String> tokens) {
		return Integer.parseInt(tokens.get(0));
	}

}
