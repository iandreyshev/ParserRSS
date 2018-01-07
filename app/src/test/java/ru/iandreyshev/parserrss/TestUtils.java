package ru.iandreyshev.parserrss;

import org.apache.commons.io.IOUtils;

public class TestUtils {
    public static String readFromFile(final String filePath) {
        try {
            return IOUtils.toString(
                    TestUtils.class.getResourceAsStream(filePath),
                    "UTF-8"
            );
        } catch (Exception ex) {
            return null;
        }
    }
}
