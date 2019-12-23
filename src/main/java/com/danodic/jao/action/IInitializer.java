package com.danodic.jao.action;

import com.danodic.jao.core.JaoLayer;
import com.danodic.jao.model.ActionModel;

public interface IInitializer {
	public void run(JaoLayer layer);
	public void loadModel(ActionModel model);
}
