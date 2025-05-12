package com.crossly.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FileUtil {

    public static String getFileString(String path) {
        try (var stream = FileUtil.class.getClassLoader().getResourceAsStream(path)) {
            if (stream == null) {
                throw new RuntimeException("File '" + path + "' not found!");
            }
            var reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder source = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line).append('\n');
            }
            return source.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
