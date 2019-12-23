package com.danodic.jao.event;

import java.util.ArrayList;

import com.danodic.jao.action.IInitializer;
import com.danodic.jao.core.JaoLayer;

/**
 * Just a class to hold and execute all initializer events. Used to detach the
 * execution of the initializers from the parsing time.
 * 
 * @author danodic
 */
public class InitializerEvent extends ArrayList<IInitializer> {
	private static final long serialVersionUID = -2275035962885848806L;

	/**
	 * Will execute all the initializer events.
	 * 
	 * @param jaoLayer The animation layer those initializers are going to run
	 *                 against.
	 */
	public void run(JaoLayer jaoLayer) {
		forEach(init -> init.run(jaoLayer));
	}

}
