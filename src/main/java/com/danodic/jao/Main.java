package com.danodic.jao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.danodic.jao.model.JaoModel;
import com.google.gson.Gson;

public class Main {

	public static void main(String[] args) throws IOException {

		String datal;
		datal = Files.readString(Paths.get("./sample.json"));

		Gson gson = new Gson();
		gson.fromJson(datal, JaoModel.class);
		
		System.out.println("debug");

	}

}
