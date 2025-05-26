package com.crossly.util;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Jude Ogboru
 */
public class FileUtil {

    public static String getFileString(String path) {
        return new String(getFileBytes(path));
    }

    public static byte[] getFileBytes(String path) {
        try {
            return Files.readAllBytes(Paths.get(getAbsoluteFilepath(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getAbsoluteFilepath(String path) {
        var url = FileUtil.class.getClassLoader().getResource(path);
        if (url != null)
            return URLDecoder.decode(url.getFile().substring(System.getProperty("os.name").contains("Windows") ? 1 : 0), Charset.defaultCharset());
        File file = new File(path);
        if (!file.isFile())
            throw new RuntimeException("File '" + path + "' does not exist!");
        return file.getAbsolutePath();
    }
}
