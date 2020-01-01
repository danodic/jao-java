package com.danodic.jao.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.danodic.jao.action.IAction;
import com.danodic.jao.core.Jao;
import com.danodic.jao.core.JaoLayer;
import com.danodic.jao.model.ActionModel;
import com.danodic.jao.parser.expressions.TimeExpressionParser;
import com.danodic.jao.renderer.IRenderer;
import com.danodic.jao.support.clocks.TimeLordClock;
import com.danodic.jao.support.libraries.actions.PulseOverTimeAction;
import com.danodic.jao.support.renderers.TestRenderer;

import org.junit.BeforeClass;
import org.testng.annotations.Test;

public class EventTest {

    String json;
    Jao jao;
    IRenderer rendererImpl;

    @BeforeClass
    public void setup() {
        rendererImpl = new TestRenderer();
    }

    @Test
    public void testAddAction() {

        Jao jao = new Jao();
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        IAction action = new PulseOverTimeAction();

        Event event = new Event();
        EventAction eAction = new EventAction(layer, action, 0L);
        event.addAction(eAction);

        assert event.getAllActionEvents().contains(eAction);
    }

    @Test
    public void testAddActions() {

        Jao jao = new Jao();
        JaoLayer layer = new JaoLayer(jao, rendererImpl);
        IAction action = new PulseOverTimeAction();

        Event event = new Event();
        List<EventAction> eActions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            eActions.add(new EventAction(layer, action, 0L));
        }
        event.addActions(eActions);

        for (EventAction eAction : eActions) {
            assert event.getAllActionEvents().contains(eAction);
        }

    }

    private EventAction getMockEventAction(JaoLayer layer, String when, String duration) {
        Map<String, String> modelData = new HashMap<>();
        ActionModel model = new ActionModel();
        IAction action = new PulseOverTimeAction();
        EventAction eAction = new EventAction(layer, action, TimeExpressionParser.parseExpression(when));

        modelData.put("duration", duration);
        model.setWhen(when);
        model.setAttributes(modelData);
        action.loadModel(model);

        return eAction;
    }

    @Test
    public void testGetRunningItems() {
        Jao jao = new Jao();
        JaoLayer layer = new JaoLayer(jao, new TestRenderer());
        TimeLordClock tardis = new TimeLordClock();
        Event event = new Event();

        jao.setClock(tardis);
        jao.addLayer(layer);
        layer.addEvent("default", event);

        EventAction eAction1 = getMockEventAction(layer, "second 0", "second 3");
        event.addAction(eAction1);

        EventAction eAction2 = getMockEventAction(layer, "second 3", "second 3");
        event.addAction(eAction2);

        for (long time : new long[] { 0L, 1000L, 2000L, 2999L }) {
            tardis.setTime(time);
            jao.render();
            assert event.getRunningItems(tardis.now()).contains(eAction1);
            assert !event.getRunningItems(tardis.now()).contains(eAction2);
        }

        tardis.setTime(3000L);
        jao.render();
        assert event.getRunningItems(tardis.now()).contains(eAction1);
        assert event.getRunningItems(tardis.now()).contains(eAction2);

        for (long time : new long[] { 3001L, 4000L, 5000L, 6000L }) {
            tardis.setTime(time);
            jao.render();
            assert !event.getRunningItems(tardis.now()).contains(eAction1);
            assert event.getRunningItems(tardis.now()).contains(eAction2);
        }

        tardis.setTime(6001L);
        jao.render();
        assert !event.getRunningItems(tardis.now()).contains(eAction1);
        assert !event.getRunningItems(tardis.now()).contains(eAction2);

    }

    @Test
    public void testCleanDone() {
        Jao jao = new Jao();
        JaoLayer layer = new JaoLayer(jao, new TestRenderer());
        TimeLordClock tardis = new TimeLordClock();
        Event event = new Event();

        jao.setClock(tardis);
        jao.addLayer(layer);
        layer.addEvent("default", event);

        EventAction eAction = getMockEventAction(layer, "second 0", "second 3");
        event.addAction(eAction);

        jao.render();
        tardis.setTime(3001L);
        jao.render();

        event.cleanDone(); // Call is done here explicitely to assure coverage is added.
        assert !event.getRunningItems(tardis.now()).contains(eAction);
    }

    @Test
    public void testIsDone() {
        Jao jao = new Jao();
        JaoLayer layer = new JaoLayer(jao, new TestRenderer());
        TimeLordClock tardis = new TimeLordClock();
        Event event = new Event();

        jao.setClock(tardis);
        jao.addLayer(layer);
        layer.addEvent("default", event);

        EventAction eAction1 = getMockEventAction(layer, "second 0", "second 3");
        event.addAction(eAction1);

        EventAction eAction2 = getMockEventAction(layer, "second 3", "second 3");
        event.addAction(eAction2);

        jao.render();
        tardis.setTime(6001L);
        jao.render();
        assert jao.isDone();

    }

    @Test
    public void testSetLoop() {
        Jao jao = new Jao();
        JaoLayer layer = new JaoLayer(jao, new TestRenderer());
        TimeLordClock tardis = new TimeLordClock();
        Event event = new Event();

        jao.setClock(tardis);
        jao.addLayer(layer);
        layer.addEvent("default", event);

        EventAction eAction1 = getMockEventAction(layer, "second 0", "second 3");
        eAction1.getAction().setLoop(false);
        event.addAction(eAction1);

        EventAction eAction2 = getMockEventAction(layer, "second 3", "second 3");
        eAction1.getAction().setLoop(false);
        event.addAction(eAction2);

        event.setLoop(true);
        assert eAction1.getAction().isLoop();
        assert eAction2.getAction().isLoop();

        event.setLoop(false);
        assert !eAction1.getAction().isLoop();
        assert !eAction2.getAction().isLoop();

    }

    @Test
    public void testReset() {
        Jao jao = new Jao();
        JaoLayer layer = new JaoLayer(jao, new TestRenderer());
        TimeLordClock tardis = new TimeLordClock();
        Event event = new Event();

        jao.setClock(tardis);
        jao.addLayer(layer);
        layer.addEvent("default", event);

        EventAction eAction1 = getMockEventAction(layer, "second 0", "second 3");
        event.addAction(eAction1);

        EventAction eAction2 = getMockEventAction(layer, "second 3", "second 3");
        event.addAction(eAction2);

        jao.render();
        tardis.setTime(6001L);
        jao.render();
        assert jao.isDone();
        assert event.getRunningItems(tardis.now()).size() == 0;

        event.reset();
        assert !jao.isDone();

        tardis.setTime(7000L);
        jao.render();
        assert event.getRunningItems(tardis.now()).contains(eAction1);

    }

    @Test
    public void testGetAllActionEvents() {
        Jao jao = new Jao();
        JaoLayer layer = new JaoLayer(jao, new TestRenderer());
        TimeLordClock tardis = new TimeLordClock();
        Event event = new Event();

        jao.setClock(tardis);
        jao.addLayer(layer);
        layer.addEvent("default", event);

        EventAction eAction1 = getMockEventAction(layer, "second 0", "second 3");
        event.addAction(eAction1);

        EventAction eAction2 = getMockEventAction(layer, "second 3", "second 3");
        event.addAction(eAction2);

        assert event.getAllActionEvents().contains(eAction1);
        assert event.getAllActionEvents().contains(eAction2);
    }
}