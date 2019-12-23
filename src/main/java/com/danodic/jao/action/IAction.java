package com.danodic.jao.action;

public interface IAction extends IInitializer {

	public void reset();

	public boolean isDone();

	public void setLoop(boolean loop);
}
