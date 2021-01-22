package com.danodic.jao.extractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import com.danodic.jao.exceptions.CannotLoadJaoFileContentException;
import com.danodic.jao.exceptions.CannotLoadJaoFileException;
import com.danodic.jao.exceptions.ContentFileDoesNotExistException;
import com.danodic.jao.exceptions.UnknownJaoFileFormatExcepton;
import com.danodic.jao.support.Defaults;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ExtractorTest {

    @DataProvider
    public Object[][] provideFileNames() {
        return new Object[][] {
            new Object[] { Defaults.SAMPLE_FOLDER },
            //new Object[] { Defaults.SAMPLE_JAO },
            //new Object[] { Defaults.SAMPLE_ZIP }
        };
    }

    @Test(dataProvider = "provideFileNames")
    public void testExtractorFactory(String filename)
            throws CannotLoadJaoFileException, CannotLoadJaoFileContentException, UnknownJaoFileFormatExcepton,
            IOException, ContentFileDoesNotExistException {

        // Load test Data
        String expectedJSON = new String(Files.readAllBytes(Paths.get(Defaults.SAMPLE_JSON_IN_FILE)));
        byte[] expectedBytes = Files.readAllBytes(Paths.get(Defaults.SAMPLE_IMAGE_IN_FILE));

        // Get the extractor and validate the data
        IExtractor extractor = ExtractorFactory.getExtractor(filename);
        assert Arrays.equals(extractor.getData("jao.png"), expectedBytes);
        assert Arrays.equals(extractor.getData("subfolder/jao2.png"), expectedBytes);
        assert extractor.getJson().equals(expectedJSON);
        
    }

    @Test(dataProvider = "provideFileNames", expectedExceptions = ContentFileDoesNotExistException.class)
    public void testContentFileDoesNotExistException(String filename)
            throws CannotLoadJaoFileException, CannotLoadJaoFileContentException, UnknownJaoFileFormatExcepton,
            ContentFileDoesNotExistException {
        IExtractor extractor = ExtractorFactory.getExtractor(filename);
        extractor.getData("jao.nope");        
    }

}