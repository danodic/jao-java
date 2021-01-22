package com.danodic.jao.action;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.danodic.jao.exceptions.CannotFindJaoActionException;
import com.danodic.jao.exceptions.CannotFindJaoInitializerException;
import com.danodic.jao.exceptions.CannotFindJaoLibraryException;
import com.danodic.jao.exceptions.CannotInstantiateJaoActionException;
import com.danodic.jao.model.ActionModel;

public class ActionFactory {

    private static Boolean factoryInitialized = false;

    // TODO: Extend a data structure just to give it a different name. Is it a good
    // idea?
    private static class ActionLibrary extends LinkedHashMap<String, Class<? extends IAction>> {

        private static final long serialVersionUID = 5319075828568574182L;
    }

    private static class InitializerLibrary extends LinkedHashMap<String, Class<? extends IInitializer>> {

        private static final long serialVersionUID = 5319075828568574182L;
    }

    private static LinkedHashMap<String, ActionLibrary> actionLibrary = new LinkedHashMap<>();
    private static LinkedHashMap<String, InitializerLibrary> initializerLibrary = new LinkedHashMap<>();

    private ActionFactory() {
    }

    /**
     * Will scan the classpath and look for classes annotated with @Action and
     * that extends IAction. This is used to find the custom-built libraries and
     * store them into a list of libraries.
     */
    public static void initializeFactory() {

        if (factoryInitialized) {
            return;
        }
        
        Reflections reflections = new Reflections(
                new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath()));

        scanInitializers(reflections);
        scanActions(reflections);
        
        factoryInitialized = true;

    }

    private static void scanInitializers(Reflections reflections) {
        List<Class<? extends IInitializer>> classes = new ArrayList<>(reflections.getSubTypesOf(IInitializer.class));
        for (Class<? extends IInitializer> clazz : classes) {
            if (clazz.isAnnotationPresent(Action.class)) {
                for (Constructor<?> cons : clazz.getConstructors()) {
                    if (cons.getParameterCount() == 0) {
                        registerInitializer(clazz);
                        break;
                    }
                }
            }
        }
    }

    private static void scanActions(Reflections reflections) {
        List<Class<? extends IAction>> classes = new ArrayList<>(reflections.getSubTypesOf(IAction.class));
        for (Class<? extends IAction> clazz : classes) {
            if (clazz.isAnnotationPresent(Action.class)) {
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
     * Will get an action and will register its informations in the proper
     * library store. If there is no library store created for this class yet,
     * it will instantiate it.
     *
     * @param action
     */
    private static void registerAction(Class<? extends IAction> action) {
        String actionName = action.getAnnotation(Action.class).name();
        String libraryName = action.getAnnotation(Action.class).library();

        if (!actionLibrary.containsKey(libraryName)) {
            actionLibrary.put(libraryName, new ActionLibrary());
        }

        actionLibrary.get(libraryName).put(actionName, action);
    }

    private static void registerInitializer(Class<? extends IInitializer> initializer) {
        String actionName = initializer.getAnnotation(Action.class).name();
        String libraryName = initializer.getAnnotation(Action.class).library();

        if (!initializerLibrary.containsKey(libraryName)) {
            initializerLibrary.put(libraryName, new InitializerLibrary());
        }

        initializerLibrary.get(libraryName).put(actionName, initializer);
    }

    /**
     * Will find an action in the action library and will return an instance of
     * it.
     *
     * @param libraryName
     * @param actionName
     * @return an instance of a class that implements IEvent.
     * @throws CannotInstantiateJaoActionException
     * @throws CannotFindJaoInitializerException
     * @throws CannotFindJaoLibraryException
     */
    public static IAction getAction(String libraryName, String actionName)
            throws CannotInstantiateJaoActionException, CannotFindJaoLibraryException, CannotFindJaoActionException {
        return getAction(libraryName, actionName, null);
    }

    /**
     * Will find an action in the action library and will return an instance of
     * it. This instance will be pre-populated with the data provided in the
     * ActionModel instance passed as argument.
     *
     * @param libraryName Name of the library to get the event from.
     * @param actionName Name of the action to be retrieved from the library.
     * @param model The data of the action parsed from the Json file.
     * @return an instance of a class that implements IAction.
     * @throws CannotInstantiateJaoActionException In case it cannot instantiate
     * the initializer.
     * @throws CannotFindJaoLibraryException
     * @throws CannotFindJaoInitializerException
     * @throws CannotFindJaoActionException
     */
    @SuppressWarnings("unchecked")
    public static IAction getAction(String libraryName, String actionName, ActionModel model)
            throws CannotInstantiateJaoActionException, CannotFindJaoLibraryException, CannotFindJaoActionException {
        IAction action;
        Constructor<IAction> defaultContructor;

        // Initialize stuff
        defaultContructor = null;

        // Handle errors
        if (!actionLibrary.containsKey(libraryName)) {
            throw new CannotFindJaoLibraryException(libraryName);
        }
        if (!actionLibrary.get(libraryName).containsKey(actionName)) {
            throw new CannotFindJaoActionException(actionName);
        }

        // Get the constructor with no parameters
        for (Constructor<?> constructor : actionLibrary.get(libraryName).get(actionName).getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                defaultContructor = (Constructor<IAction>) constructor;
                break;
            }
        }

        // Throw an error in case the constructor was not found
        if (defaultContructor == null) {
            throw new CannotInstantiateJaoActionException(actionName,
                    new RuntimeException("No argument-free constructor was found for action."));
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
            throw new CannotInstantiateJaoActionException(actionName, e);
        }

    }

    /**
     * Will find an initializer in the initializer library and will return an
     * instance of it.
     *
     * @param libraryName
     * @param initializerName
     * @return an instance of a class that implements IInitializer.
     * @throws CannotInstantiateJaoActionException
     * @throws CannotFindJaoInitializerException
     * @throws CannotFindJaoLibraryException
     */
    public static IInitializer getInitializer(String libraryName, String initializerName)
            throws CannotInstantiateJaoActionException, CannotFindJaoLibraryException,
            CannotFindJaoInitializerException {
        return getInitializer(libraryName, initializerName, null);
    }

    /**
     * Will find an initializer in the initializer library and will return an
     * instance of it. This instance will be pre-populated with the data
     * provided in the ActionModel instance passed as argument.
     *
     * @param libraryName Name of the library to get the initializer from.
     * @param initializeName Name of the initializer to be retrieved from the
     * library.
     * @param model The data of the action parsed from the Json file.
     * @return an instance of a class that implements IInitializer.
     * @throws CannotInstantiateJaoActionException In case it cannot instantiate
     * the initializer.
     * @throws CannotFindJaoLibraryException
     * @throws CannotFindJaoInitializerException
     */
    @SuppressWarnings("unchecked")
    public static IInitializer getInitializer(String libraryName, String initializeName, ActionModel model)
            throws CannotInstantiateJaoActionException, CannotFindJaoLibraryException,
            CannotFindJaoInitializerException {
        IInitializer initializer;
        Constructor<IInitializer> defaultContructor;

        // Initialize stuff
        defaultContructor = null;

        // Handle errors
        if (!initializerLibrary.containsKey(libraryName)) {
            throw new CannotFindJaoLibraryException(libraryName);
        }
        if (!initializerLibrary.get(libraryName).containsKey(initializeName)) {
            throw new CannotFindJaoInitializerException(initializeName);
        }

        // Get the constructor with no parameters
        for (Constructor<?> constructor : initializerLibrary.get(libraryName).get(initializeName).getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                defaultContructor = (Constructor<IInitializer>) constructor;
                break;
            }
        }

        // Throw an error in case the constructor was not found
        if (defaultContructor == null) {
            throw new CannotInstantiateJaoActionException(initializeName,
                    new RuntimeException("No argument-free constructor was found for initializer."));
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
            throw new CannotInstantiateJaoActionException(initializeName, e);
        }

    }

}
