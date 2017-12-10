package ru.iandreyshev.parserrss.models.rss;

import org.junit.Test;

import static org.junit.Assert.*;

public class RssTest {
    private static final String URL = "www.url.com";

    @Test
    public void return_after_parsing_null_string() {
        final String rssText = null;

        assertNull(Rss.Parser.parse(rssText, URL));
    }

    @Test
    public void return_null_after_parsing_null_bytes_array() {
        final byte[] array = null;

        assertNull(Rss.Parser.parse(array, URL));
    }

}