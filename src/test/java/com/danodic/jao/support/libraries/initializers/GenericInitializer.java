package com.danodic.jao.support.libraries.initializers;

import java.io.PrintStream;

import com.danodic.jao.action.IInitializer;
import com.danodic.jao.core.JaoLayer;
import com.danodic.jao.model.ActionModel;
import com.danodic.jao.support.Defaults;

public class GenericInitializer implements IInitializer {

    private PrintStream outStream = Defaults.defaultPrintStream;
    private ActionModel model;

    private boolean done = false;

    @Override
    public void run(JaoLayer layer) {
        outStream.println(this.getClass().getName() + " Class Initializer Run.\nEntries:\n");
        if(model==null) {
            outStream.println("\tmodel is null");
        } else {
            model.getAttributes().forEach((key, value) -> outStream.println(key + ":" + value));
        }
        done = true;
    }

    @Override
    public void loadModel(ActionModel model) {
        this.model = model;
    }

    public PrintStream getOutStream() {
        return outStream;
    }

    public void setOutStream(PrintStream outStream) {
        this.outStream = outStream;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
    
}