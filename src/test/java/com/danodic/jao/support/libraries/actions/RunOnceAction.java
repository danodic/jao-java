package com.danodic.jao.support.libraries.actions;

import com.danodic.jao.action.Action;
import com.danodic.jao.core.JaoLayer;

@Action(library = "jao.unittest", name = "RunOnceAction")
public class RunOnceAction extends GenericAction {

    public RunOnceAction() {
        super();
    }

    @Override
    public void run(JaoLayer layer) {
        super.run(layer);
        super.setDone(true);
    }

}