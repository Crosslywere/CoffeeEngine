package com.crossly.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author Jude Ogboru (ChatGPT)
 */
public class FileUtil {

	public static String getFileString(String path) {
		return new String(getFileBytes(path), StandardCharsets.UTF_8);
	}

	public static byte[] getFileBytes(String path) {
		try (InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(path)) {
			if (in == null) {
				throw new FileNotFoundException("Resource not found: " + path);
			}
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] chunk = new byte[4096];
			int n;
			while ((n = in.read(chunk)) != -1) {
				buffer.write(chunk, 0, n);
			}
			return buffer.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("Failed to read resource: " + path, e);
		}
	}

	public static File extractResourceToTempFile(String path, String prefix, String suffix) {
		try (InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(path)) {
			if (in == null) {
				throw new FileNotFoundException("Resource not found: " + path);
			}

			File tempFile = File.createTempFile(prefix, suffix);
			tempFile.deleteOnExit();

			try (OutputStream out = new FileOutputStream(tempFile)) {
				out.write(in.readAllBytes());
			}

			return tempFile;

		} catch (IOException e) {
			throw new RuntimeException("Failed to extract resource to temp file: " + path, e);
		}
	}
}
