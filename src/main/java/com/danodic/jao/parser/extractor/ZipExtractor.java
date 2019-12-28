package com.danodic.jao.parser.extractor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

import com.danodic.jao.exceptions.CannotLoadJaoFileContentException;
import com.danodic.jao.exceptions.CannotLoadJaoFileException;
import com.danodic.jao.exceptions.ContentFileDoesNotExistException;

/**
 * This is the implementation of the extractor for zip files. The .jao file is
 * expected to be just a zip file renamed to .jao, so we use the standard java
 * zip implementation for this.
 */
public class ZipExtractor implements IExtractor {

	private String json;
	private Map<String, byte[]> data;

	/**
	 * @param filePath The name of the file to be loaded.
	 * @throws CannotLoadJaoFileException        In case the file could not be
	 *                                           loaded by some reason.
	 * @throws CannotLoadJaoFileContentException In case one of the files inside the
	 *                                           jao file could not be loaded by
	 *                                           some reason.
	 */
	public ZipExtractor(String filePath) throws CannotLoadJaoFileException, CannotLoadJaoFileContentException {

		ZipFile file;
		ZipEntry entry;
		Enumeration<? extends ZipEntry> entries;
		InputStream inputStream;

		// Initialize data
		data = new HashMap<>();
		json = null;

		// Create the zip file object
		try {
			file = new ZipFile(filePath);
		} catch (IOException e) {
			throw new CannotLoadJaoFileException(filePath, e);
		}

		try {

			// Get the entries
			entries = file.entries();

			// Go over each entry
			while (entries.hasMoreElements()) {

				try {
					// Get the entry
					entry = entries.nextElement();

					// Get the input stream
					inputStream = file.getInputStream(entry);

					// Check if this is the json or just data
					if (entry.getName().equals("jao.json")) {
						json = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
						continue;
					}

					// Just add the file to the entry list
					data.put(entry.getName(), IOUtils.toByteArray(inputStream));

				} catch (IOException e) {
					throw new CannotLoadJaoFileContentException(filePath, e);
				}
			}

		} finally {
			try {
				file.close();
			} catch (IOException e) {
			} // There is no need to handle exception to close file as we just want to finish
			  // up with this file handling.
		}

		// Throw an exception in case we have no json
		if (json == null) {
			throw new CannotLoadJaoFileException(filePath,
					new RuntimeException("No file named jao.json has been found."));
		}
	}

	/**
	 * Will return the bytes from the given file.
	 * 
	 * @throws ContentFileDoesNotExistException In case the file was not found.
	 */
	public byte[] getData(String name) throws ContentFileDoesNotExistException {
		if (!data.containsKey(name))
			throw new ContentFileDoesNotExistException(name);
		return data.get(name);
	}

	/**
	 * Will return the JSON string from the jao file.
	 */
	public String getJson() {
		return this.json;
	}

}
