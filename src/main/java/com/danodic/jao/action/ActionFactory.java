package com.danodic.jao.action;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.danodic.jao.exceptions.CannotInstantiateJaoActiontException;
import com.danodic.jao.model.ActionModel;

public class ActionFactory {

	// TODO: Extend a data structure just to give it a different name. Is it a good
	// idea?
	private static class ActionLibrary extends LinkedHashMap<String, Class<? extends IAction>> {
		private static final long serialVersionUID = 5319075828568574182L;
	}

	private static class InitializerLibrary extends LinkedHashMap<String, Class<? extends IInitializer>> {
		private static final long serialVersionUID = 5319075828568574182L;
	}

	private static LinkedHashMap<String, ActionLibrary> actionLibrary = new LinkedHashMap<String, ActionLibrary>();
	private static LinkedHashMap<String, InitializerLibrary> initializerLibrary = new LinkedHashMap<String, InitializerLibrary>();

	/**
	 * Will scan the classpath and look for classes annotated with @Action and that
	 * extends IAction. This is used to find the custom-built libraries and store
	 * them into a list of libraries.
	 */
	public static void initializeFactory() {

		List<Class<? extends IInitializer>> classes;

		// Intialize the reflections library
		Reflections reflections = new Reflections(
				new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath()));

		// Get all classes that implement IAction
		classes = new ArrayList<Class<? extends IInitializer>>(reflections.getSubTypesOf(IInitializer.class));

		// For each one of them, check if they implement the @Action annotation
		for (Class<? extends IInitializer> clazz : classes) {
			if (clazz.isAnnotationPresent(Action.class)) {

				// Now check if it has a constructor without parameters
				for (Constructor<?> cons : clazz.getConstructors()) {
					if (cons.getParameterCount() == 0) {
						registerAction(clazz);
						break;
					}
				}
			}
		}
	}

	/**
	 * Will get an action and will register its informations in the proper library
	 * store. If there is no library store created for this class yet, it will
	 * instantiate it.
	 * 
	 * @param action
	 */
	@SuppressWarnings("unchecked")
	private static void registerAction(Class<? extends IInitializer> action) {

		String libraryName, actionName;

		// Get the annotation to pull the names from
		actionName = action.getAnnotation(Action.class).name();
		libraryName = action.getAnnotation(Action.class).library();

		// Check if this is an initializer or an action
		if (action.isAssignableFrom(IAction.class)) {
			if (!actionLibrary.containsKey(libraryName))
				actionLibrary.put(libraryName, new ActionLibrary());
			actionLibrary.get(libraryName).put(actionName, (Class<? extends IAction>) action);
		} else {
			if (!initializerLibrary.containsKey(libraryName))
				initializerLibrary.put(libraryName, new InitializerLibrary());
			initializerLibrary.get(libraryName).put(actionName, action);
		}
	}

	/**
	 * Will find an action in the action library and will return an instance of it.
	 * 
	 * @param libraryName
	 * @param actionName
	 * @return an instance of a class that implements IEvent.
	 * @throws CannotInstantiateJaoActiontException
	 */
	public static IAction getAction(String libraryName, String actionName) throws CannotInstantiateJaoActiontException {
		return getEvent(libraryName, actionName, null);
	}

	/**
	 * Will find an action in the action library and will return an instance of it.
	 * This instance will be pre-populated with the data provided in the ActionModel
	 * instance passed as argument.
	 * 
	 * @param libraryName Name of the library to get the event from.
	 * @param actionName  Name of the action to be retrieved from the library.
	 * @param model       The data of the action parsed from the Json file.
	 * @return an instance of a class that implements IAction.
	 * @throws CannotInstantiateJaoActiontException In case it cannot instantiate
	 *                                              the initializer.
	 */
	@SuppressWarnings("unchecked")
	public static IAction getEvent(String libraryName, String actionName, ActionModel model)
			throws CannotInstantiateJaoActiontException {
		IAction action;
		Constructor<IAction> defaultContructor;

		// Initialize stuff
		defaultContructor = null;

		// Get the constructor with no parameters
		for (Constructor<?> constructor : actionLibrary.get(libraryName).get(actionName).getConstructors()) {
			if (constructor.getParameterCount() == 0) {
				defaultContructor = (Constructor<IAction>) constructor;
				break;
			}
		}

		try {
			// Get the action from the library and instantiate it
			action = defaultContructor.newInstance();

			// In case the call has a model, load it
			if (model != null) {
				action.loadModel(model);
				action.reset();
			}

			return action;

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new CannotInstantiateJaoActiontException(actionName, e);
		}

	}
	
	/**
	 * Will find an initializer in the initializer library and will return an instance of it.
	 * 
	 * @param libraryName
	 * @param initializerName
	 * @return an instance of a class that implements IInitializer.
	 * @throws CannotInstantiateJaoActiontException
	 */
	public static IInitializer getInitializer(String libraryName, String initializerName) throws CannotInstantiateJaoActiontException {
		return getEvent(libraryName, initializerName, null);
	}

	/**
	 * Will find an initializer in the initializer library and will return an instance of it.
	 * This instance will be pre-populated with the data provided in the ActionModel
	 * instance passed as argument.
	 * 
	 * @param libraryName Name of the library to get the initializer from.
	 * @param actionName  Name of the initializer to be retrieved from the library.
	 * @param model       The data of the action parsed from the Json file.
	 * @return an instance of a class that implements IInitializer.
	 * @throws CannotInstantiateJaoActiontException In case it cannot instantiate
	 *                                              the initializer.
	 */
	@SuppressWarnings("unchecked")
	public static IInitializer getInitializer(String libraryName, String actionName, ActionModel model)
			throws CannotInstantiateJaoActiontException {
		IInitializer initializer;
		Constructor<IInitializer> defaultContructor;

		// Initialize stuff
		defaultContructor = null;

		// Get the constructor with no parameters
		for (Constructor<?> constructor : initializerLibrary.get(libraryName).get(actionName).getConstructors()) {
			if (constructor.getParameterCount() == 0) {
				defaultContructor = (Constructor<IInitializer>) constructor;
				break;
			}
		}

		try {
			// Get the action from the library and instantiate it
			initializer = defaultContructor.newInstance();

			// In case the call has a model, load it
			if (model != null) {
				initializer.loadModel(model);
			}

			return initializer;

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new CannotInstantiateJaoActiontException(actionName, e);
		}

	}

}
