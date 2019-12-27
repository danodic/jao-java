package com.danodic.jao.support.libraries.initializers;

import com.danodic.jao.action.Action;
import com.danodic.jao.action.IInitializer;
import com.danodic.jao.core.JaoLayer;
import com.danodic.jao.model.ActionModel;

@Action(library = "jao.unittest", name = "BadInitializer")
public class BadInitializer implements IInitializer {

    public BadInitializer() {
        throw new RuntimeException("This is not meant to work.");
    }

    @Override
    public void run(JaoLayer layer) {
    }

    @Override
    public void loadModel(ActionModel model) {
    }
    
}