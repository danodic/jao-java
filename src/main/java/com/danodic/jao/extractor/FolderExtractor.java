package com.danodic.jao.extractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.danodic.jao.exceptions.CannotLoadJaoFileContentException;
import com.danodic.jao.exceptions.CannotLoadJaoFileException;
import com.danodic.jao.exceptions.ContentFileDoesNotExistException;

/**
 * This is the implementation of the extractor for folders. While the .jao file
 * is a cool way of carrying the data, loading data from folders are helpful
 * mainly during development.
 */
public class FolderExtractor implements IExtractor {

    private String json;
    private Map<String, byte[]> data;
    private String rootFolder;

    /**
     * @param filePath The name of the file to be loaded.
     * @throws CannotLoadJaoFileException In case the file could not be loaded
     * by some reason.
     * @throws CannotLoadJaoFileContentException In case one of the files inside
     * the jao file could not be loaded by some reason.
     */
    public FolderExtractor(String folder) throws CannotLoadJaoFileException, CannotLoadJaoFileContentException {

        Path folderPath;

        // Initialize data
        folderPath = Paths.get(folder);
        data = new HashMap<>();
        json = null;
        rootFolder = folder;

        // Check if the folder exists
        if (!folderPath.toFile().exists()) {
            throw new CannotLoadJaoFileException(folder, new RuntimeException("Folder provided does not exists."));
        }

        // Check if this is not a directory
        if (!folderPath.toFile().isDirectory()) {
            throw new CannotLoadJaoFileException(folder, new RuntimeException("Path provided is not a folder."));
        }

        // Parse the folder and load the bytes in memory
        parseFolder(folder);

        // Throw an exception in case we have no json
        if (json == null) {
            throw new CannotLoadJaoFileException(folder,
                    new RuntimeException("No file named jao.json has been found."));
        }
    }

    /**
     * Will iterate over a folder and call itself in case another folder is
     * found within the structure.
     *
     * @param folder Name of the folder.
     * @throws CannotLoadJaoFileContentException In case it can`t read the file.
     */
    private void parseFolder(String folder) throws CannotLoadJaoFileContentException {

        // List files from the folder and iterate over them
        for (File entry : new File(folder).listFiles()) {
            if (entry.isDirectory()) {
                parseFolder(entry.getPath());
            } else {
                try {
                    // Check if this is the json file
                    if (entry.getName().equals("jao.json")) {
                        json = new String(Files.readAllBytes(Paths.get(entry.getPath())));
                        continue;
                    }

                    // Add file to the collection
                    String path = entry.toString().substring(rootFolder.length(), entry.toString().length());
                    path = path.replaceAll("[\\\\]", "/");
                    if (path.startsWith("/") || path.startsWith("\\")) {
                        path = path.substring(1, path.length());
                    }
                    data.put(path, Files.readAllBytes(Paths.get(entry.getPath())));

                } catch (IOException e) {
                    throw new CannotLoadJaoFileContentException(entry.getPath(), e);
                }
            }
        }
    }

    /**
     * Will return the bytes from the given file.
     *
     * @throws ContentFileDoesNotExistException In case the file was not found.
     */
    public byte[] getData(String name) throws ContentFileDoesNotExistException {
        if (!data.containsKey(name)) {
            throw new ContentFileDoesNotExistException(name);
        }
        return data.get(name);
    }

    /**
     * Will return the JSON string from the jao file.
     */
    public String getJson() {
        return this.json;
    }

    @Override
    public String getFilename() {
        return rootFolder;
    }

}
