package com.danodic.jao.core;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.danodic.jao.action.IInitializer;
import com.danodic.jao.event.Event;
import com.danodic.jao.event.EventAction;
import com.danodic.jao.event.InitializerEvent;
import com.danodic.jao.exceptions.CannotFindJaoActionException;
import com.danodic.jao.exceptions.CannotFindJaoInitializerException;
import com.danodic.jao.exceptions.CannotFindJaoLibraryException;
import com.danodic.jao.exceptions.CannotInstantiateJaoActionException;
import com.danodic.jao.exceptions.CannotInstantiateJaoRenderer;
import com.danodic.jao.exceptions.JaoEventNotFoundException;
import com.danodic.jao.renderer.IRenderer;
import com.danodic.jao.support.clocks.TimeLordClock;
import com.danodic.jao.support.libraries.actions.GenericAction;
import com.danodic.jao.support.libraries.actions.RunOnceAction;
import com.danodic.jao.support.libraries.initializers.GenericInitializer;
import com.danodic.jao.support.libraries.initializers.RunCountInitializer;
import com.danodic.jao.support.renderers.TestRenderer;
import com.danodic.jao.time.StandardClock;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class JaoLayerTest {

    private Jao jao;
    private IRenderer rendererImpl;

    @BeforeTest(alwaysRun = true)
    public void setup() throws IOException, CannotFindJaoLibraryException, CannotFindJaoInitializerException,
            CannotFindJaoActionException, CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer {
        jao = new Jao();
        rendererImpl = new TestRenderer();
    }

    /**
     * The GenericInitializer will return true at isDone in case it has been
     * executed. Use this to check if JaoLayer.initialize() is invoking the
     * initializer.
     */
    @Test
    public void testInitialize() {

        GenericInitializer initializer = new GenericInitializer();
        JaoLayer layer = new JaoLayer(jao, rendererImpl);

        assert !initializer.isDone();
        layer.addInitializer(initializer);

        initializer.setDone(false);
        layer.initialize();
        assert initializer.isDone();
    }

    /**
     * When calling reset, two things must happen: all initializers must be executed
     * again and the reset() method from each action must be called.
     */
    @Test
    public void testReset() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        Event event = new Event();

        // Setup the action
        EventAction aEvent;
        RunOnceAction action1 = new RunOnceAction();
        RunOnceAction action2 = new RunOnceAction();
        aEvent = new EventAction(layer, action1, 0L);
        event.addAction(aEvent);
        aEvent = new EventAction(layer, action2, 1000L);
        event.addAction(aEvent);

        // Setup the initializers
        InitializerEvent iEvent = new InitializerEvent();
        List<RunCountInitializer> initializers = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            initializers.add(new RunCountInitializer());
        }
        iEvent.addAll(initializers);

        // Setup the layer
        layer.addInitializers(iEvent);
        layer.addEvent("sample_event", event);
        layer.setEvent("sample_event");

        // Make everything done
        layer.render(4000L);

        // Now, check that everything has run
        for(RunCountInitializer initializer : initializers)
            assert initializer.getRunCount() == 1;
        assert layer.isDone();
        assert action1.isDone();
        assert action2.isDone();

        // Run the reset and check if reset has been invoked
        layer.reset();
        for(RunCountInitializer initializer : initializers)
            assert initializer.getRunCount() == 2;
        assert !layer.isDone();
        assert !action1.isDone();
        assert !action2.isDone();

    }

    /**
     * Just check if an instance of LayerParamaters is returned.
     */
    @Test
    public void testGetParameters() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        assert layer.getParameters() instanceof LayerParameters;
    }

    /**
     * Add an event with a given name into the layer. Check if testHasEvent returns
     * true for it. Test with an invalid name and check it returns false.
     */
    @Test
    public void testHasEvent() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        Event event = new Event();

        layer.addEvent("sample_event", event);
        assert layer.hasEvent("sample_event");
        assert !layer.hasEvent("something else");
    }

    /**
     * If no event name is provided, getEvent() must return the current event. The
     * current event is initially null.
     */
    @Test
    public void testGetEventCurrent() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        Event event = new Event();

        assert layer.getEvent() == null;

        layer.addEvent("sample_event", event);
        layer.setEvent("sample_event");

        assert layer.getEvent() == event;
    }

    /**
     * When the name of the event is provided, getEvent() must return the
     * corresponding event.
     * 
     * @throws JaoEventNotFoundException
     */
    @Test
    public void testGetEvent() throws JaoEventNotFoundException {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        Event event1 = new Event();
        Event event2 = new Event();

        layer.addEvent("first_event", event1);
        layer.addEvent("second_event", event2);

        assert layer.getEvent("first_event") == event1;
        assert layer.getEvent("second_event") == event2;
    }

    /**
     * When lloking explicitely for an event and it doesn`t exists, getEvent() must
     * throw JaoEventNotFoundException.
     * 
     * @throws JaoEventNotFoundException
     */
    @Test(expectedExceptions = JaoEventNotFoundException.class)
    public void testJaoEventNotFoundException() throws JaoEventNotFoundException {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        layer.getEvent("wrong");
    }

    /**
     * In case a valid event name is provided, the layer must return this event at
     * getEvent() later. Else, it must return null.
     */
    @Test
    public void testSetEvent() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        Event event = new Event();
        layer.addEvent("sample_event", event);

        layer.setEvent("sample_event");
        assert layer.getEvent() == event;

        layer.setEvent("invalid");
        assert layer.getEvent() == null;

    }

    /**
     * When setting the current event to an invalid name, the layer must return null
     * at getLayer().
     */
    public void testSetEventInvalidName() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        Event event = new Event();
        layer.addEvent("sample_event", event);

        layer.setEvent("invalid");
        assert layer.getEvent() == null;
    }

    /**
     * When a InitializerEvent is added, all initializers inside this event must be
     * executed and added to the initializer list. Since the list of initializers is
     * private, we inspect the initializers themselves to check if they have been
     * run.
     */
    @Test
    public void testAddInitializers() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        InitializerEvent event = new InitializerEvent();

        List<IInitializer> initializers = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            initializers.add(new GenericInitializer());
        }

        event.addAll(initializers);
        layer.addInitializers(event);

        for (IInitializer initializer : initializers) {
            assert ((GenericInitializer) initializer).isDone();
            ((GenericInitializer) initializer).setDone(false);
        }

        layer.initialize();
        for (IInitializer initializer : initializers) {
            assert ((GenericInitializer) initializer).isDone();
        }

    }

    /**
     * When an event is added using addEvents(), it must be found by hasEvent().
     */
    @Test
    public void testAddEvents() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        Event event1 = new Event();
        Event event2 = new Event();

        Map<String, Event> events = new HashMap<>();
        events.put("event1", event1);
        events.put("event2", event2);

        layer.addEvents(events);
        assert layer.hasEvent("event1");
        assert layer.hasEvent("event2");
    }

    public void testSetEventAndReset() {

    }

    /**
     * The renderer used to instantiate the layer must be returned using
     * getRenderer().
     */
    @Test
    public void testGetRenderer() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        assert layer.getRenderer() == rendererImpl;
    }

    /**
     * When setting a new renderer using setRenderer(), the new renderer must be
     * returned using getRenderer().
     */
    @Test
    public void testSetRenderer() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        TestRenderer newRenderer = new TestRenderer();
        assert layer.getRenderer() == rendererImpl;
        layer.setRenderer(newRenderer);
        assert layer.getRenderer() == newRenderer;
    }

    /**
     * When render() is called, it must invoke render() from all actions in the
     * current action. In this test, we use the RunOnceAction() that will return
     * true at isDone() after its render() method is called at least once.
     */
    @Test
    public void testRender() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        Event event = new Event();
        GenericAction action = new RunOnceAction();
        EventAction aEvent = new EventAction(layer, action, 0L);

        event.addAction(aEvent);
        layer.addEvent("sample_event", event);

        assert !action.isDone();
        layer.setEvent("sample_event");
        layer.render(0L);
        assert action.isDone();
    }

    /**
     * When adding an event, this event must be returned by getEvent(String).
     * 
     * @throws JaoEventNotFoundException
     */
    @Test
    public void testAddEvent() throws JaoEventNotFoundException {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        Event event = new Event();

        assert !layer.hasEvent("sample_event");
        layer.addEvent("sample_event", event);
        assert layer.getEvent("sample_event") == event;
    }

    /**
     * The method isDone() must return true when the current EventAction returns
     * true at isDone().
     */
    @Test
    public void testIsDone() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        Event event = new Event();

        GenericAction action1 = new RunOnceAction();
        GenericAction action2 = new RunOnceAction();

        EventAction aEvent = new EventAction(layer, action1, 0L);
        event.addAction(aEvent);

        aEvent = new EventAction(layer, action2, 4000L);
        event.addAction(aEvent);

        layer.addEvent("sample_event", event);
        layer.setEvent("sample_event");

        assert !layer.isDone();

        layer.render(0L);
        assert !layer.isDone();
        assert action1.isDone();
        assert !action2.isDone();

        layer.render(4000L);
        assert action1.isDone();
        assert action2.isDone();
        assert layer.isDone();
    }

    /**
     * The method getScaleFactor() must return whatever scale factor is set at the
     * main Jao instance.
     */
    @Test
    public void testGetScaleFactor() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        assert Float.valueOf(layer.getScaleFactor()).compareTo(1.0f) == 0;
        jao.setScaleFactor(2.0f);
        assert Float.valueOf(layer.getScaleFactor()).compareTo(2.0f) == 0;
    }

    /**
     * The method getElapsed() must get the current elapsed time from the Jao main
     * instance.
     */
    @Test
    public void testGetElapsed() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        TimeLordClock epoch = new TimeLordClock();
        jao.setClock(epoch);

        epoch.setTime(0L);
        jao.render();
        assert layer.getElapsed() == 0L;

        epoch.setTime(1000L);
        jao.render();
        assert layer.getElapsed() == 1000L;

    }

    /**
     * The method getFrameTime() must get from the Jao instance the time in
     * milliseconds in which the last frame was triggered.
     */
    @Test
    public void testGetFrameTime() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        TimeLordClock epoch = new TimeLordClock();
        jao.setClock(epoch);

        epoch.setTime(1000L);
        Long before = Instant.now().toEpochMilli();
        jao.render();
        Long after = Instant.now().toEpochMilli();
        assert before <= layer.getLastFrameTime();
        assert layer.getLastFrameTime() <= after;

    }

    /**
     * When setLoop() is invoked, all actions inside the current event must have the
     * loop set to the given value.
     */
    @Test
    public void testSetLoop() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        Event event = new Event();
        GenericAction action = new GenericAction();
        EventAction aEvent = new EventAction(layer, action, 0L);

        event.addAction(aEvent);
        layer.addEvent("sample_event", event);

        action.setLoop(false);
        assert !action.isLoop();

        layer.setEvent("sample_event");
        layer.setLoop(true);
        assert action.isLoop();
    }

    /**
     * When adding an initializer, it must be executed right after addInitializer()
     * is invoked. In case another initialalizer is added, both must run.
     */
    @Test
    public void testAddInitializer() {
        GenericInitializer initializer1 = new GenericInitializer();
        GenericInitializer initializer2 = new GenericInitializer();
        JaoLayer layer = new JaoLayer(jao, rendererImpl);

        assert !initializer1.isDone();
        layer.addInitializer(initializer1);
        assert initializer1.isDone();

        initializer1.setDone(false);
        assert !initializer1.isDone();

        layer.addInitializer(initializer2);
        assert initializer1.isDone();
        assert initializer2.isDone();
    }

    /**
     * The method testGetClock() must return the clock currently used by the main
     * Jao instance.
     */
    @Test
    public void testGetClock() {
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        assert layer.getClock() == jao.getClock();

        StandardClock newClock = new StandardClock();
        jao.setClock(newClock);
        assert layer.getClock() == newClock;
    }

}