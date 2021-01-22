package com.danodic.jao.model;

import java.util.HashMap;
import java.util.Map;

public class DataTypeModel {

    private String type;
    private Map<String, String> attributes;

    public String getType() {
        return type;
    }

    public void setType(String name) {
        this.type = name;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
    
    @Override
    public DataTypeModel clone() {
        Map<String, String> data = new HashMap<>();
        attributes.forEach((key, value) -> data.put(key, value));
        
        DataTypeModel clone = new DataTypeModel();
        clone.setAttributes(data);
        
        return clone;
    }

}
