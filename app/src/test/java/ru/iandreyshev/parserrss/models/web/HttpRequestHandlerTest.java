package ru.iandreyshev.parserrss.models.web;

import org.junit.Test;

import static org.junit.Assert.*;

public class HttpRequestHandlerTest {
    private static final String VALID_URL = "http://domain.com";
    private static final String URL_WITHOUT_PROTOCOL = "domain.com";
    private static final String URL_WITH_PORT = "http://domain.com:80/";

    private HttpRequestHandler mHandler;

    @Test
    public void return_invalid_url_state_after_send_with_null_url() {
        final String nullUrl = null;
        mHandler = new HttpRequestHandler(nullUrl);
        mHandler.sendGet();

        assertEquals(HttpRequestHandler.State.BadUrl, mHandler.getState());
    }

    @Test
    public void return_not_send_state_after_create() {
        mHandler = new HttpRequestHandler(VALID_URL);

        assertEquals(HttpRequestHandler.State.NotSend, mHandler.getState());
    }

    @Test
    public void return_bad_url_after_init_with_url_without_protocol() {
        mHandler = new HttpRequestHandler(URL_WITHOUT_PROTOCOL);

        assertEquals(mHandler.getState(), HttpRequestHandler.State.BadUrl);
    }

    @Test
    public void return_not_send_after_init_from_url_with_port() {
        mHandler = new HttpRequestHandler(URL_WITH_PORT);

        assertEquals(HttpRequestHandler.State.NotSend, mHandler.getState());
    }
}
