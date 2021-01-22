package com.danodic.jao.action;

import com.danodic.jao.core.JaoLayer;
import com.danodic.jao.model.ActionModel;

/**
 * Represents a single action inside a Jao schema.
 *
 * @author danodic
 */
public interface IAction {

    public void run(JaoLayer layer);

    public void loadModel(ActionModel model);

    public void reset();

    public boolean isDone();

    public void setLoop(boolean loop);

    public boolean isLoop();

    public IAction clone();
}
