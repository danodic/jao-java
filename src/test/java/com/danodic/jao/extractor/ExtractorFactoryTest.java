package com.danodic.jao.extractor;

import com.danodic.jao.exceptions.CannotLoadJaoFileContentException;
import com.danodic.jao.exceptions.CannotLoadJaoFileException;
import com.danodic.jao.exceptions.UnknownJaoFileFormatExcepton;
import com.danodic.jao.support.Defaults;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ExtractorFactoryTest {

    @DataProvider
    public Object[][] provideFileNames() {
        return new Object[][] {
            new Object[] {Defaults.SAMPLE_FOLDER, FolderExtractor.class},
            new Object[] {Defaults.SAMPLE_JAO, ZipExtractor.class},
            new Object[] {Defaults.SAMPLE_ZIP, ZipExtractor.class}
        };
    }

    @Test(dataProvider = "provideFileNames")
    public void testExtractorFactory(String filename, Class <? extends IExtractor> clazz)
            throws CannotLoadJaoFileException, CannotLoadJaoFileContentException, UnknownJaoFileFormatExcepton {
        assert ExtractorFactory.getExtractor(filename).getClass().isAssignableFrom(clazz);
    }

    @Test(expectedExceptions = UnknownJaoFileFormatExcepton.class)
    public void testUnknownJaoFileFormatExcepton()
            throws CannotLoadJaoFileException, CannotLoadJaoFileContentException, UnknownJaoFileFormatExcepton {
        ExtractorFactory.getExtractor(Defaults.SAMPLE_BAD_EXTENSION);
    }

}