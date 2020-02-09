package com.danodic.jao.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.danodic.jao.exceptions.CannotFindJaoActionException;
import com.danodic.jao.exceptions.CannotFindJaoInitializerException;
import com.danodic.jao.exceptions.CannotFindJaoLibraryException;
import com.danodic.jao.exceptions.CannotInstantiateJaoActionException;
import com.danodic.jao.exceptions.CannotInstantiateJaoRenderer;
import com.danodic.jao.exceptions.ContentFileDoesNotExistException;
import com.danodic.jao.support.Defaults;
import com.danodic.jao.support.renderers.BadRenderer;
import com.danodic.jao.support.renderers.TestRenderer;

import org.testng.annotations.Test;

public class JaoParserTest {

    @Test
    public void testParseJson() throws CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer,
            CannotFindJaoLibraryException, CannotFindJaoInitializerException, CannotFindJaoActionException,
            IOException, ContentFileDoesNotExistException {
        String json = new String(Files.readAllBytes(Paths.get(Defaults.SAMPLE_JSON)));
        JaoParser.parseJson(json, null, TestRenderer.class);
    }

    @Test(expectedExceptions = CannotInstantiateJaoActionException.class)
    public void testCannotInstantiateJaoActionException() throws IOException, CannotFindJaoLibraryException, CannotFindJaoInitializerException,
            CannotFindJaoActionException, CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer, ContentFileDoesNotExistException {
        String json = new String(Files.readAllBytes(Paths.get(Defaults.SAMPLE_BAD_ACTION)));
        JaoParser.parseJson(json, null, TestRenderer.class);
    }

    @Test(expectedExceptions = CannotInstantiateJaoActionException.class)
    public void testtestCannotInstantiateJaoActionException() throws IOException, CannotFindJaoLibraryException, CannotFindJaoInitializerException,
            CannotFindJaoActionException, CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer, ContentFileDoesNotExistException {
        String json = new String(Files.readAllBytes(Paths.get(Defaults.SAMPLE_BAD_INITIALIZER)));
        JaoParser.parseJson(json, null, TestRenderer.class);
    }
    
    @Test(expectedExceptions = CannotInstantiateJaoRenderer.class)
    public void testCannotInstantiateJaoRenderer()
            throws CannotFindJaoLibraryException, CannotFindJaoInitializerException, CannotFindJaoActionException,
            CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer, IOException, ContentFileDoesNotExistException {
        String json = new String(Files.readAllBytes(Paths.get(Defaults.SAMPLE_JSON)));
        JaoParser.parseJson(json, null, BadRenderer.class);
    }
    
    @Test(expectedExceptions = CannotFindJaoInitializerException.class)
    public void testCannotFindJaoInitializerException() throws IOException, CannotFindJaoLibraryException, CannotFindJaoInitializerException,
            CannotFindJaoActionException, CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer, ContentFileDoesNotExistException {
        String json = new String(Files.readAllBytes(Paths.get(Defaults.SAMPLE_WRONG_INITIALIZER_NAME)));
        JaoParser.parseJson(json, null, TestRenderer.class);
    }

    @Test(expectedExceptions = CannotFindJaoLibraryException.class)
    public void testCannotFindJaoActionException()
            throws CannotFindJaoLibraryException, CannotFindJaoInitializerException, CannotFindJaoActionException,
            CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer, IOException, ContentFileDoesNotExistException {
        String json = new String(Files.readAllBytes(Paths.get(Defaults.SAMPLE_WRONG_LIB_NAME_ACTION)));
        JaoParser.parseJson(json, null, TestRenderer.class);
    }

    @Test(expectedExceptions = CannotFindJaoLibraryException.class)
    public void testCannotFindJaoLibraryExceptionAction()
            throws CannotFindJaoLibraryException, CannotFindJaoInitializerException, CannotFindJaoActionException,
            CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer, IOException, ContentFileDoesNotExistException {
        String json = new String(Files.readAllBytes(Paths.get(Defaults.SAMPLE_WRONG_LIB_NAME_INITIALIZER)));
        JaoParser.parseJson(json, null, TestRenderer.class);
    }

}