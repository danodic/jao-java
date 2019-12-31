package com.danodic.jao.support.libraries.actions;

import com.danodic.jao.action.Action;
import com.danodic.jao.core.JaoLayer;
import com.danodic.jao.model.ActionModel;
import com.danodic.jao.parser.expressions.TimeExpressionParser;

@Action(library = "jao.unittest", name = "PulseOverTime")
public class PulseOverTimeAction extends GenericAction {

    private long duration;
    private long when;

    public PulseOverTimeAction() {
        super();
        duration = 0l;
        when = 0l;
    }

    @Override
    public void run(JaoLayer layer) {
        super.run(layer);
        if(layer.getElapsed() > when + duration) {
            super.setDone(true);
        }
    }

    public void loadModel(ActionModel model) {
        super.loadModel(model);
        
        if(super.getModel().getWhen()!=null)
            when = TimeExpressionParser.parseExpression(super.getModel().getWhen());

        if (super.getModel().getAttributes().containsKey("duration")) {
            duration = TimeExpressionParser.parseExpression(super.getModel().getAttributes().get("duration"));
        }
    }
}