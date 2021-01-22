package com.danodic.jao.support.libraries.actions;

import com.danodic.jao.action.Action;
import com.danodic.jao.action.IAction;
import com.danodic.jao.core.JaoLayer;
import com.danodic.jao.model.ActionModel;

@Action(library = "jao.unittest", name = "BadAction")
public class BadAction implements IAction {

    public BadAction() {
        throw new RuntimeException("This is not meant to work.");
    }

    @Override
    public void run(JaoLayer layer) {
    }

    @Override
    public void loadModel(ActionModel model) {
    }

    @Override
    public void reset() {
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public void setLoop(boolean loop) {
    }

    public boolean isLoop() {
        return false;
    }

    @Override
    public IAction clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}