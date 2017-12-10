package ru.iandreyshev.parserrss.models.web;

import org.junit.Test;

import static org.junit.Assert.*;

public class UrlTest {
    private static final String VALID_URL = "http://domain.com/";

    @Test
    public void return_null_if_parse_null_string() {
        assertNull(Url.parse(null));
    }

    @Test
    public void return_null_if_parse_empty_string() {
        assertNull(Url.parse(""));
    }

    @Test
    public void return_url_string_in_toString_method() {
        final Url url = Url.parse(VALID_URL);

        assertNotNull(url);
        assertEquals(VALID_URL, url.toString());
    }
}