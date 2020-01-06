package com.danodic.jao.support.libraries.actions;

import com.danodic.jao.action.Action;
import com.danodic.jao.core.JaoLayer;

@Action(library = "jao.unittest", name = "RunOnceAction")
public class RunOnceAction extends GenericAction {

    private long duration;
    private long when;

    public RunOnceAction() {
        super();
        duration = 0l;
        when = 0l;
    }

    @Override
    public void run(JaoLayer layer) {
        super.run(layer);
        super.setDone(true);
    }

}