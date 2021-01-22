package com.danodic.jao.support.libraries.actions;

import java.io.PrintStream;

import com.danodic.jao.action.IAction;
import com.danodic.jao.core.JaoLayer;
import com.danodic.jao.model.ActionModel;
import com.danodic.jao.support.Defaults;

public class GenericAction implements IAction {

    private PrintStream outStream = Defaults.defaultPrintStream;
    private ActionModel model;
    private boolean done;
    private boolean loop;

    public GenericAction() {
        done = false;
        loop = false;
    }

    @Override
    public void run(JaoLayer layer) {
        outStream.println(this.getClass().getName() + " Class Action Run.\nEntries:\n");
        if (model == null) {
            outStream.println("\tmodel is null");
        } else {
            model.getAttributes().forEach((key, value) -> outStream.println(key + ":" + value));
        }
    }

    @Override
    public void loadModel(ActionModel model) {
        this.model = model;
    }

    @Override
    public void reset() {
        done = false;
        outStream.println("Resetting " + this.getClass().getName());
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public void setLoop(boolean loop) {
        this.loop = loop;
        outStream.println("Set loop of class " + this.getClass().getName() + " to " + loop);
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isLoop() {
        return loop;
    }

    public ActionModel getModel() {
        return model;
    }

    @Override
    public IAction clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}