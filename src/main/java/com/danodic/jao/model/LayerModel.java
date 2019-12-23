package com.danodic.jao.model;

import java.util.List;

public class LayerModel {

	private DataTypeModel dataType;
	private List<EventModel> events;

	public DataTypeModel getDataType() {
		return dataType;
	}

	public void setDataType(DataTypeModel dataType) {
		this.dataType = dataType;
	}

	public List<EventModel> getEvents() {
		return events;
	}

	public void setEvents4(List<EventModel> events) {
		this.events = events;
	}

}
