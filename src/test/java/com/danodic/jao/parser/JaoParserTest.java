package com.danodic.jao.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.danodic.jao.core.Jao;
import com.danodic.jao.exceptions.CannotFindJaoActionException;
import com.danodic.jao.exceptions.CannotFindJaoInitializerException;
import com.danodic.jao.exceptions.CannotFindJaoLibraryException;
import com.danodic.jao.exceptions.CannotInstantiateJaoActionException;
import com.danodic.jao.exceptions.CannotInstantiateJaoRenderer;
import com.danodic.jao.support.Defaults;
import com.danodic.jao.support.renderers.TestRenderer;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class JaoParserTest {

    private String json;

    @BeforeClass
    public void setup() throws IOException {
        json = new String(Files.readAllBytes(Paths.get(Defaults.SAMPLE_JSON)));
    }

    @Test
    public void testParseAction() throws CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer,
            CannotFindJaoLibraryException, CannotFindJaoInitializerException, CannotFindJaoActionException {
        // Get the JAO instance and validate it
        Jao jao = JaoParser.parseJson(json, TestRenderer.class);
    }

}