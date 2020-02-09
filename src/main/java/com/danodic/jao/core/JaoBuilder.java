package com.danodic.jao.core;

import com.danodic.jao.exceptions.CannotFindJaoActionException;
import com.danodic.jao.exceptions.CannotFindJaoInitializerException;
import com.danodic.jao.exceptions.CannotFindJaoLibraryException;
import com.danodic.jao.exceptions.CannotInstantiateJaoActionException;
import com.danodic.jao.exceptions.CannotInstantiateJaoRenderer;
import com.danodic.jao.exceptions.CannotLoadJaoFileContentException;
import com.danodic.jao.exceptions.CannotLoadJaoFileException;
import com.danodic.jao.exceptions.ContentFileDoesNotExistException;
import com.danodic.jao.exceptions.UnknownJaoFileFormatExcepton;
import com.danodic.jao.extractor.ExtractorFactory;
import com.danodic.jao.extractor.IExtractor;
import com.danodic.jao.parser.JaoParser;
import com.danodic.jao.renderer.IRenderer;

public class JaoBuilder {

	public static Jao getJaoFromJson(String filename, Class<? extends IRenderer> renderer)
			throws CannotLoadJaoFileException, CannotLoadJaoFileContentException, UnknownJaoFileFormatExcepton,
			CannotFindJaoLibraryException, CannotFindJaoInitializerException, CannotFindJaoActionException,
			CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer, ContentFileDoesNotExistException {

		IExtractor extractor = ExtractorFactory.getExtractor(filename);
		Jao jao = JaoParser.parseJson(extractor.getJson(), extractor, renderer);

		return jao;
	}
	
	public static Jao getJaoFromJson(IExtractor extractor, Class<? extends IRenderer> renderer)
			throws CannotLoadJaoFileException, CannotLoadJaoFileContentException, UnknownJaoFileFormatExcepton,
			CannotFindJaoLibraryException, CannotFindJaoInitializerException, CannotFindJaoActionException,
			CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer, ContentFileDoesNotExistException {

		Jao jao = JaoParser.parseJson(extractor.getJson(), extractor, renderer);

		return jao;
	}

}
