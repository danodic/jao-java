package com.danodic.jao.core;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.danodic.jao.exceptions.CannotFindJaoActionException;
import com.danodic.jao.exceptions.CannotFindJaoInitializerException;
import com.danodic.jao.exceptions.CannotFindJaoLibraryException;
import com.danodic.jao.exceptions.CannotInstantiateJaoActionException;
import com.danodic.jao.exceptions.CannotInstantiateJaoRenderer;
import com.danodic.jao.parser.JaoParser;
import com.danodic.jao.renderer.IRenderer;
import com.danodic.jao.support.Defaults;
import com.danodic.jao.support.renderers.TestRenderer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.when;


@PrepareForTest(Instant.class)
@RunWith(PowerMockRunner.class)
public class JaoTest {

    String json;
    Jao jao;
    IRenderer rendererImpl;

    @Before
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
}