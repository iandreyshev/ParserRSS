package ru.iandreyshev.parserrss;

import org.apache.commons.io.IOUtils;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import java.io.StringReader;

public class TestUtils {
    private static final String DISABLE_DTD_FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

    public static Document readXmlFromFile(final String filePath) {
        try {
            final String xml = readFromFile(filePath);
            final SAXBuilder builder = new SAXBuilder();
            builder.setFeature(DISABLE_DTD_FEATURE, false);

            return builder.build(new StringReader(xml));

        } catch (Exception ex) {
            return null;
        }
    }

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
