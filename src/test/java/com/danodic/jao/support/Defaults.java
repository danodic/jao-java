package com.danodic.jao.support;

import java.io.PrintStream;

public class Defaults {

    public static final String SAMPLE_JSON = "./src/test/resources/sample.json";
    
    public static final String SAMPLE_JAO = "./src/test/resources/files/jao.jao";
    public static final String SAMPLE_ZIP = "./src/test/resources/files/jao.zip";
    public static final String SAMPLE_FOLDER = "./src/test/resources/files/jaoasfolder/";
    public static final String SAMPLE_BAD_EXTENSION = "./src/test/resources/files/jao.nope";

    public static final String SAMPLE_JSON_IN_FILE = "./src/test/resources/files/jaoasfolder/jao.json";
    public static final String SAMPLE_IMAGE_IN_FILE = "./src/test/resources/files/jaoasfolder/jao.png";

    public static final String SAMPLE_BAD_ACTION = "./src/test/resources/sample_bad_action.json";
    public static final String SAMPLE_BAD_INITIALIZER = "./src/test/resources/sample_bad_initializer.json";
    public static final String SAMPLE_WRONG_ACTION_NAME = "./src/test/resources/sample_wrong_action_name.json";
    public static final String SAMPLE_WRONG_INITIALIZER_NAME = "./src/test/resources/sample_wrong_initializer_name.json";
    public static final String SAMPLE_WRONG_LIB_NAME_ACTION = "./src/test/resources/sample_wrong_lib_name_action.json";
    public static final String SAMPLE_WRONG_LIB_NAME_INITIALIZER = "./src/test/resources/sample_wrong_lib_name_initializer.json";

	public static PrintStream defaultPrintStream = System.out;
    
}