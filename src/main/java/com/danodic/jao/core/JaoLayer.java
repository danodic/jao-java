package com.danodic.jao.core;

import java.util.List;
import java.util.Map;

import com.danodic.jao.action.IInitializer;
import com.danodic.jao.event.Event;
import com.danodic.jao.event.InitializerEvent;
import com.danodic.jao.renderer.IRenderer;

public class JaoLayer {

	private IRenderer renderer;
	private Map<String, Event> events;
	private List<IInitializer> initializers;

	public JaoLayer(IRenderer rendererImpl) {
		this.renderer = rendererImpl;
	}

	public void addInitializers(InitializerEvent initializers) {
		this.initializers.addAll(initializers);
	}

	public void addEvents(Map<String, Event> events) {
		this.events.putAll(events);
	}

}
