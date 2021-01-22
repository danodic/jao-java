package com.danodic.jao.extractor;

import com.danodic.jao.exceptions.ContentFileDoesNotExistException;

/**
 * An extractor is a class that will parse some kind of JAO format (ex.: a .jao
 * file or a folder). It is used when parsing the folder/files and providing the
 * data stored into that structure in a standard way.
 * 
 * It must load the contents of the folder/file provided and store in a byte
 * array in memory. Then, its data will be accessed through the getData method.
 * 
 * @author danodic
 */
public interface IExtractor {

    /**
     * 
     * @param filename Name of the file to get the bytes from.
     * @return A byte array representing the file contents.
     * @throws ContentFileDoesNotExistException In case the file request does not
     *                                          exists in the JAO file.
     */
    public byte[] getData(String filename) throws ContentFileDoesNotExistException;

    /**
     * Must return the string representation of the JSON file (jao.json).
     * 
     * @return Contents of jao.json.
     */
    public String getJson();

    /**
     * Return the name of the file loaded by this extractor.
     * 
     * @return Name of the file.
     */
    public String getFilename();

}
