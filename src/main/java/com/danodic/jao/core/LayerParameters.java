package com.danodic.jao.core;

import java.util.HashMap;

/**
 * This is just a hash map with some custom methods to make it more practical to
 * use. This hash map is used to holde the parameters of a given layer.
 */
@SuppressWarnings("serial")
public class LayerParameters extends HashMap<String, Object> {

	/**
	 * Will return the value of a given parameter and will cast it to String.
	 */
	public String getAsString(String paramName) {
		return (String) (containsKey(paramName) ? get(paramName) : "");
	}

	/**
	 * Will return the value of a given parameter and will cast it to Integer.
	 */
	public Integer getAsInteger(String paramName) {
		return (Integer) (containsKey(paramName) ? get(paramName) : 0);
	}

	/**
	 * Will return the value of a given parameter and will cast it to Long.
	 */
	public Long getAsLong(String paramName) {
		return (Long) (containsKey(paramName) ? get(paramName) : 0l);
	}

	/**
	 * Will return the value of a given parameter and will cast it to Float.
	 */
	public Float getAsFloat(String paramName) {
		return (Float) (containsKey(paramName) ? get(paramName) : 0f);
	}

	/**
	 * Will return the value of a given parameter and will cast it to Double.
	 */
	public Double getAsDouble(String paramName) {
		return (Double) (containsKey(paramName) ? get(paramName) : 0d);
	}

	/**
	 * Will return the value of a given parameter and will cast it to Boolean.
	 */
	public Boolean getAsBoolean(String paramName) {
		return (Boolean) (containsKey(paramName) ? get(paramName) : false);
	}

}
