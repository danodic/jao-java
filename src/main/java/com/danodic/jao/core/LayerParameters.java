package com.danodic.jao.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class LayerParameters extends HashMap<String, Object>{
	
	public void setParameter(String paramName, Object parameter) {
		put(paramName, parameter);
	}
	
	public String getAsString(String paramName) {
		return (String) get(paramName);
	}
	
	public Integer getAsInteger(String paramName) {
		return (Integer) get(paramName);
	}
	
	public Long getAsLong(String paramName) {
		return (Long) get(paramName);
	}
	
	public Float getAsFloat(String paramName) {
		return (Float) (containsKey(paramName)? get(paramName) : 1f);
	}
	
	public Double getAsDouble(String paramName) {
		return (Double) get(paramName);
	}
	
	public Double getAsBoolean(String paramName) {
		return (Double) get(paramName);
	}
	
	public List<?> getAsList(String paramName) {
		return (List<?>) get(paramName);
	}
	
	public Map<?,?> getAsMap(String paramName) {
		return (Map<?,?>) get(paramName);
	}
	
	public Object getAsObject(String paramName) {
		return (Object) get(paramName);
	}

}
