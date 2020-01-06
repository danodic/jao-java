package com.danodic.jao.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.danodic.jao.event.Event;
import com.danodic.jao.event.EventAction;
import com.danodic.jao.exceptions.CannotFindJaoActionException;
import com.danodic.jao.exceptions.CannotFindJaoInitializerException;
import com.danodic.jao.exceptions.CannotFindJaoLibraryException;
import com.danodic.jao.exceptions.CannotInstantiateJaoActionException;
import com.danodic.jao.exceptions.CannotInstantiateJaoRenderer;
import com.danodic.jao.parser.JaoParser;
import com.danodic.jao.renderer.IRenderer;
import com.danodic.jao.support.Defaults;
import com.danodic.jao.support.clocks.TimeLordClock;
import com.danodic.jao.support.libraries.actions.PulseOverTimeAction;
import com.danodic.jao.support.renderers.TestRenderer;
import com.danodic.jao.time.StandardClock;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class JaoTest {

    String json;
    Jao jao;
    IRenderer rendererImpl;

    @BeforeMethod(alwaysRun = true)
    public void setup() throws IOException, CannotFindJaoLibraryException, CannotFindJaoInitializerException,
            CannotFindJaoActionException, CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer {
        json = new String(Files.readAllBytes(Paths.get(Defaults.SAMPLE_JSON)));
        jao = JaoParser.parseJson(json, TestRenderer.class);
        rendererImpl = new TestRenderer();
    }

    @Test
    public void testAddLayer() {
        JaoLayer newLayer = new JaoLayer(jao, rendererImpl);
        assert !jao.getLayers().contains(newLayer);

        jao.addLayer(newLayer);
        assert jao.getLayers().contains(newLayer);
    }

    @Test
    public void testAddLayers() {
        List<JaoLayer> layers = new ArrayList<JaoLayer>();
        for (int i = 0; i < 10; i++) {
            layers.add(new JaoLayer(jao, rendererImpl));
        }

        for (JaoLayer layer : layers) {
            assert !jao.getLayers().contains(layer);
        }

        jao.addLayers(layers);
        for (JaoLayer layer : layers) {
            assert jao.getLayers().contains(layer);
        }
    }

    @Test
    public void testGetElapsed() {
        TimeLordClock theTimeAndSpaceParadox = new TimeLordClock();
        jao.setClock(theTimeAndSpaceParadox);
        jao.render();
        assert jao.getElapsed() == 0L;
        
        theTimeAndSpaceParadox.setTime(3000L);
        jao.render();
        assert jao.getElapsed() == 3000L;
    }

    @Test
    public void testGetLastFrameTime() {
        TimeLordClock theTimeAndSpaceParadox = new TimeLordClock();
        jao.setClock(theTimeAndSpaceParadox);
        
        theTimeAndSpaceParadox.setTime(6000L);
        Long before = Instant.now().toEpochMilli();
        jao.render();
        Long after = Instant.now().toEpochMilli();

        assert before <= jao.getLastFrameTime();
        assert jao.getLastFrameTime() <= after;
    }

    @Test
    public void testGetLastFrameDelta() {
        TimeLordClock theTimeAndSpaceParadox = new TimeLordClock();
        jao.setClock(theTimeAndSpaceParadox);
        jao.render();
        assert jao.getLastFrameTime() == 0L;

        theTimeAndSpaceParadox.setTime(3000L);
        jao.render();

        theTimeAndSpaceParadox.setTime(6000L);
        jao.render();

        assert jao.getLastFrameDelta() == 3000L;
    }

    @Test
    public void testSetClock() {
        TimeLordClock theTimeAndSpaceParadox = new TimeLordClock();
        jao.setClock(theTimeAndSpaceParadox);
        assert jao.getClock() == theTimeAndSpaceParadox;
    }

    @Test
    public void testGetClock() {
        assert jao.getClock() instanceof StandardClock;
    }

    @Test
    public void testSetEvent() {
        Event event = new Event();
        jao.getLayers().forEach(layer -> layer.addEvent("testEvent", event));

        assert jao.getLayers().size() > 0;
        for (JaoLayer layer : jao.getLayers())
            assert layer.getEvent() != event;

        jao.setEvent("testEvent");
        for (JaoLayer layer : jao.getLayers())
            assert layer.getEvent() == event;
    }

    @Test
    public void testGetLayers() {
        JaoLayer newLayer = new JaoLayer(jao, rendererImpl);
        jao.addLayer(newLayer);

        assert jao.getLayers().get(jao.getLayers().size() - 1) == newLayer;
    }

    @Test
    public void testSetScaleFactor() {
        jao.setScaleFactor(2.5f);
        assert jao.getScaleFactor() == 2.5f;
    }

    @Test
    public void testGetScaleFactor() {
        assert jao.getScaleFactor() == 1f;
    }

    @Test
    public void testIsDone() {

        TimeLordClock delorean = new TimeLordClock();
        jao.setClock(delorean);
        assert jao.isDone() == false;
        
        delorean.setTime(0L);
        jao.render();
        assert jao.isDone() == false;

        delorean.setTime(5000L);
        jao.render();
        assert jao.isDone() == true;

    }

    @Test 
    public void testRenderEmptyLayer() throws CannotFindJaoLibraryException, CannotFindJaoInitializerException,
            CannotFindJaoActionException, CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer {
        
        // We need a fresh instance for this test
        Jao jao = new Jao();

        // Generate a bunch of mock layers and events into defaultEvent
        List<JaoLayer> layers = new ArrayList<>();
        for(int i=0; i<10; i++) {
            JaoLayer layer = new JaoLayer(jao, rendererImpl);
            layers.add(layer);
        }

        jao.addLayers(layers);
        jao.render();
    }

    @Test 
    public void testPrepareToFinish() throws CannotFindJaoLibraryException, CannotFindJaoInitializerException,
            CannotFindJaoActionException, CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer {
        
        // We need a fresh instance for this test
        Jao jao = new Jao();

        // Generate a bunch of mock layers and events into defaultEvent
        List<JaoLayer> layers = new ArrayList<>();
        for(int i=0; i<10; i++) {
            JaoLayer layer = new JaoLayer(jao, rendererImpl);
            layers.add(layer);
            
            Event event = new Event();
            layer.addEvent("default", event);
            
            EventAction eAction = new EventAction(layer, new PulseOverTimeAction(), 0L);
            event.addAction(eAction);
        }

        jao.addLayers(layers);

        // Make sure all events have loop == true
        for(JaoLayer layer : layers) {
            for(EventAction eAction : layer.getEvent().getAllActionEvents()) {
                eAction.getAction().setLoop(true);
            }
        }

        // This is supposed to set all actions to loop == false in the currentEvent.
        jao.prepareToFinish();
        for(JaoLayer layer : layers) {
            for(EventAction eAction : layer.getEvent().getAllActionEvents()) {
                assert eAction.getAction().isLoop() == false;
            }
        }
            
    }
}