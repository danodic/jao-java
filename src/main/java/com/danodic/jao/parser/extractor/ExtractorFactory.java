package com.danodic.jao.parser.extractor;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.danodic.jao.exceptions.CannotLoadJaoFileContentException;
import com.danodic.jao.exceptions.CannotLoadJaoFileException;
import com.danodic.jao.exceptions.UnknownJaoFileFormatExcepton;

/**
 * Provides an instance of an extractor based on the format of the file
 * provided.
 * 
 * @author danodic
 *
 */
public class ExtractorFactory {

	/**
	 * Provides an instance of IExtractor according to known file types.
	 * 
	 * @param filename Name of the file to be loaded.
	 * @return An instance of IExtractor compatible with the file type provided.
	 * @throws CannotLoadJaoFileContentException In case any of the content files
	 *                                           could not be loaded.
	 * @throws CannotLoadJaoFileException        In case the jao file could not be
	 *                                           loaded.
	 * @throws UnknownJaoFileFormatExcepton      In case of a unknown file format is
	 *                                           provided.
	 */
	public static IExtractor getExtractor(String filename)
			throws CannotLoadJaoFileException, CannotLoadJaoFileContentException, UnknownJaoFileFormatExcepton {

		String lastName;

		// Check if the file provided is a folder
		if (Files.isDirectory(Paths.get(filename))) {
			return new FolderExtractor(filename);
		}

		// Not a folder, check if we have a file extension
		lastName = Paths.get(filename).getName(-1).toString();

		// Check if this is a known file format
		if (lastName.toLowerCase().endsWith(".zip") || lastName.toLowerCase().endsWith(".jao")) {
			return new ZipExtractor(filename);
		}

		// If not a known extension, throw an exception
		throw new UnknownJaoFileFormatExcepton(filename);

	}

}
