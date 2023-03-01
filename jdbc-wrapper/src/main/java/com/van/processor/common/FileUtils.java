package com.van.processor.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public abstract class FileUtils {
	public static File getFileFromResources(String fileName) {
		ClassLoader classLoader = FileUtils.class.getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException(String.format("File '%s' is not found!", fileName));
		} else {
			return new File(resource.getFile());
		}
	}

	public static String stringFomFile(File file) throws IOException {
		assert file != null : "File should not be null";
		StringBuilder sb = new StringBuilder();
		try (FileReader reader = new FileReader(file); BufferedReader br = new BufferedReader(reader)) {
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		}
		return sb.toString();
	}
}
