package com.danodic.jao.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.danodic.jao.core.Jao;
import com.danodic.jao.exceptions.CannotFindJaoInitializerException;
import com.danodic.jao.exceptions.CannotFindJaoLibraryException;
import com.danodic.jao.exceptions.CannotInstantiateJaoActiontException;
import com.danodic.jao.exceptions.CannotInstantiateJaoRenderer;
import com.danodic.jao.support.renderers.TestRenderer;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class JaoParserTests {

    private String JSON_SAMPLE_PATH = "./src/test/resources/sample.json";

    private String json;

    @BeforeClass
    public void setup() throws IOException {
        json = new String(Files.readAllBytes(Paths.get(JSON_SAMPLE_PATH)));
    }

    @Test
    public void testParseAction() throws CannotInstantiateJaoActiontException, CannotInstantiateJaoRenderer,
            CannotFindJaoLibraryException, CannotFindJaoInitializerException {
        // Get the JAO instance and validate it
        Jao jao = JaoParser.parseJson(json, TestRenderer.class);
    }

}