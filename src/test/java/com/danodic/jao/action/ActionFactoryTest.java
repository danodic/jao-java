package com.danodic.jao.action;

import java.util.HashMap;

import com.danodic.jao.exceptions.CannotFindJaoActionException;
import com.danodic.jao.exceptions.CannotFindJaoInitializerException;
import com.danodic.jao.exceptions.CannotFindJaoLibraryException;
import com.danodic.jao.exceptions.CannotInstantiateJaoActionException;
import com.danodic.jao.model.ActionModel;

import org.testng.annotations.Test;

public class ActionFactoryTest {

    private final String LIBRARY_NAME = "jao.unittest";

    @Test()
    public void testInitializeFactory() {
        ActionFactory.initializeFactory();
    }

    @Test(dependsOnMethods = "testInitializeFactory")
    public void testGetActionWithoutModel()
            throws CannotFindJaoLibraryException, CannotFindJaoActionException, CannotInstantiateJaoActionException {
        ActionFactory.getAction(LIBRARY_NAME, "PulseOverTime");
    }

    @Test(dependsOnMethods = "testInitializeFactory")
    public void testGetActionWithModel()
            throws CannotFindJaoLibraryException, CannotFindJaoActionException, CannotInstantiateJaoActionException {
        ActionModel model = new ActionModel();
        model.setAttributes(new HashMap<String, String>());
        ActionFactory.getAction(LIBRARY_NAME, "PulseOverTime", model);
    }

    @Test(dependsOnMethods = "testInitializeFactory")
    public void testGetInitializerWithoutModel() throws CannotFindJaoLibraryException,
            CannotFindJaoInitializerException, CannotInstantiateJaoActionException {
        ActionFactory.getInitializer(LIBRARY_NAME, "Opacity");
    }

    @Test(dependsOnMethods = "testInitializeFactory")
    public void testGetInitializerWithModel() throws CannotFindJaoLibraryException, CannotFindJaoInitializerException,
            CannotInstantiateJaoActionException {
        ActionModel model = new ActionModel();
        model.setAttributes(new HashMap<String, String>());
        ActionFactory.getInitializer(LIBRARY_NAME, "Opacity", model);
    }

    @Test(dependsOnMethods = "testInitializeFactory", expectedExceptions = CannotFindJaoActionException.class)
    public void testCannotFindJaoActionException()
            throws CannotFindJaoLibraryException, CannotInstantiateJaoActionException, CannotFindJaoActionException {
        ActionFactory.getAction(LIBRARY_NAME, "InvalidAction");
    }

    @Test(dependsOnMethods = "testInitializeFactory", expectedExceptions = CannotFindJaoInitializerException.class)
    public void testCannotFindJaoInitializerException() throws CannotFindJaoLibraryException,
            CannotFindJaoInitializerException, CannotInstantiateJaoActionException {
        ActionFactory.getInitializer(LIBRARY_NAME, "InvalidInitializer");
    }

    @Test(dependsOnMethods = "testInitializeFactory", expectedExceptions = CannotFindJaoLibraryException.class)
    public void testCannotFindJaoLibraryException() throws CannotFindJaoLibraryException,
            CannotFindJaoInitializerException, CannotInstantiateJaoActionException {
        ActionFactory.getInitializer("jao.invalid", "");
    }

    @Test(dependsOnMethods = "testInitializeFactory", expectedExceptions = CannotInstantiateJaoActionException.class)
    public void testCannotInstantiateJaoActionExceptionInitializer() throws CannotFindJaoLibraryException,
            CannotFindJaoInitializerException, CannotInstantiateJaoActionException {
        ActionFactory.getInitializer("jao.unittest", "BadInitializer");
    }

    @Test(dependsOnMethods = "testInitializeFactory", expectedExceptions = CannotInstantiateJaoActionException.class)
    public void testCannotInstantiateJaoActionExceptionAction() throws CannotFindJaoLibraryException,
            CannotInstantiateJaoActionException, CannotFindJaoActionException {
        ActionFactory.getAction("jao.unittest", "BadAction");
    }
}