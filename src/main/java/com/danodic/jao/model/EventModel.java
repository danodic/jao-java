package com.danodic.jao.model;

import java.util.List;

public class EventModel {

	private String name;
	private List<ActionModel> actions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ActionModel> getActions() {
		return actions;
	}

	public void setActions(List<ActionModel> actions) {
		this.actions = actions;
	}

}
