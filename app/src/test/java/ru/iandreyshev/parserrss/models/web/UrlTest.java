package ru.iandreyshev.parserrss.models.web;

import org.junit.Test;

import static org.junit.Assert.*;

public class UrlTest {
    private static final String VALID_URL = "http://domain.com/";
    private static final String URL_WITHOUT_PROTOCOL = "domain.com/";
    private static final String URL_WITH_PORT = "http://domain.com:8080/";
    private static final String URL_WITH_SUB_DOMAIN = "http://domain.com.ru/";

    @Test
    public void returnNullIfParseNullString() {
        assertNull(Url.parse(null));
    }

    @Test
    public void returnNullIfParseEmptyString() {
        assertNull(Url.parse(""));
    }

    @Test
    public void returnUrlStringInToStringMethod() {
        final Url url = Url.parse(VALID_URL);

        assertNotNull(url);
        assertEquals(VALID_URL, url.toString());
    }
}