package com.danodic.jao.event;

import com.danodic.jao.action.IAction;
import com.danodic.jao.core.JaoLayer;

public class EventAction implements Comparable<EventAction>{
	
	private JaoLayer animation;
	private IAction action; 
	private long when;
	
	public EventAction(JaoLayer animation, IAction action, long when) {
		// Store stuff
		this.animation = animation;
		this.action = action;
		this.when = when;
	}
	
	public void run() {
		action.run(animation);
	}
	
	public boolean isStartTime(long startTime) {
		return when >= startTime;
	}

	public double getWhen() {
		return when;
	}

	public void setWhen(long when) {
		this.when = when;
	}

	public IAction getAction() {
		return action;
	}

	public void setAction(IAction action) {
		this.action = action;
	}

	public int compareTo(EventAction other) {
		return Double.compare(when, other.getWhen());
	}

	public boolean isDone() {
		return action.isDone();
	}

	public void reset() {
		action.reset();
	}

	public void setLoop(boolean loop) {
		action.setLoop(loop);
	}

}
