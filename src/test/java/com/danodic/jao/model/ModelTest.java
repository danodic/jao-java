package com.danodic.jao.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.danodic.jao.support.Defaults;
import com.google.gson.Gson;

import org.testng.annotations.Test;

public class ModelTest {

    @Test
    public void testDeserialization() throws IOException {
        Gson gson = new Gson();
        String json = new String(Files.readAllBytes(Paths.get(Defaults.SAMPLE_JSON)));
        gson.fromJson(json, JaoModel.class);
    }

    @Test
    public void testJaoModelContents() throws IOException {

        // Deserialize into the model class
        Gson gson = new Gson();
        String json = new String(Files.readAllBytes(Paths.get(Defaults.SAMPLE_JSON)));
        JaoModel model = gson.fromJson(json, JaoModel.class);

        // Validate data matches the json
        assert model.getLayers().size() == 1;
        LayerModel layer = model.getLayers().get(0);
        
        DataTypeModel dataModel = layer.getDataType();
        assert dataModel.getType().equals("sprite");
        assert dataModel.getAttributes().containsKey("path");
        assert dataModel.getAttributes().get("path").equals("jao.png");

        List<EventModel> events = layer.getEvents();
        assert events.size() == 3;

        EventModel initializeEvent = events.get(0);
        assert initializeEvent.getName().equals("initialize");
        assert initializeEvent.getActions().size() == 1;

        ActionModel opacityAction = initializeEvent.getActions().get(0);
        assert opacityAction.getName().equals("Opacity");
        assert opacityAction.getLibrary().equals("jao.unittest");
        assert opacityAction.getAttribute().equals("0");

        EventModel defaultEvent = events.get(1);
        assert defaultEvent.getName().equals("default");
        assert defaultEvent.getActions().size() == 1;

        ActionModel pulseAction = defaultEvent.getActions().get(0);
        assert pulseAction.getLibrary().equals("jao.unittest");
        assert pulseAction.getName().equals("PulseOverTime");
        assert pulseAction.getWhen().equals("second 0");

        assert pulseAction.getAttributes().containsKey("start_opacity");
        assert pulseAction.getAttributes().containsKey("end_opacity");
        assert pulseAction.getAttributes().containsKey("duration");
        assert pulseAction.getAttributes().containsKey("bounce");
        assert pulseAction.getAttributes().containsKey("loop");

        assert pulseAction.getAttributes().get("start_opacity").equals("0");
        assert pulseAction.getAttributes().get("end_opacity").equals("1");
        assert pulseAction.getAttributes().get("duration").equals("seconds 1");
        assert pulseAction.getAttributes().get("bounce").equals("true");
        assert pulseAction.getAttributes().get("loop").equals("true");

        EventModel somethingElseEvent = events.get(2);
        assert somethingElseEvent.getName().equals("something_else");
        assert somethingElseEvent.getActions().size() == 1;

        pulseAction = somethingElseEvent.getActions().get(0);
        assert pulseAction.getLibrary().equals("jao.unittest");
        assert pulseAction.getName().equals("PulseOverTime");
        assert pulseAction.getWhen().equals("second 0");

        assert pulseAction.getAttributes().containsKey("start_opacity");
        assert pulseAction.getAttributes().containsKey("end_opacity");
        assert pulseAction.getAttributes().containsKey("duration");
        assert pulseAction.getAttributes().containsKey("bounce");
        assert pulseAction.getAttributes().containsKey("loop");

        assert pulseAction.getAttributes().get("start_opacity").equals("0");
        assert pulseAction.getAttributes().get("end_opacity").equals("1");
        assert pulseAction.getAttributes().get("duration").equals("seconds 2");
        assert pulseAction.getAttributes().get("bounce").equals("true");
        assert pulseAction.getAttributes().get("loop").equals("true");

    }

}