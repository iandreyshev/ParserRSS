package ru.iandreyshev.parserrss.models.web;

import org.junit.Test;

import static org.junit.Assert.*;

public class HttpRequestHandlerTest {
    private static final String VALID_URL = "http://domain.com";
    private static final String URL_WITHOUT_PROTOCOL = "domain.com";
    private static final String URL_WITH_PORT = "http://domain.com:80/";

    private HttpRequestHandler mHandler;

    @Test
    public void returnInvalidUrlStateAfterSendWithNullUrl() {
        final String nullUrl = null;
        mHandler = new HttpRequestHandler(nullUrl);
        mHandler.sendGet();

        assertEquals(HttpRequestHandler.State.BadUrl, mHandler.getState());
    }

    @Test
    public void returnNotSendStateAfterCreate() {
        mHandler = new HttpRequestHandler(VALID_URL);

        assertEquals(HttpRequestHandler.State.NotSend, mHandler.getState());
    }

    @Test
    public void returnBadUrlAfterInitWithUrlWithoutProtocol() {
        mHandler = new HttpRequestHandler(URL_WITHOUT_PROTOCOL);

        assertEquals(mHandler.getState(), HttpRequestHandler.State.BadUrl);
    }

    @Test
    public void returnNotSendAfterInitFromUrlWithPort() {
        mHandler = new HttpRequestHandler(URL_WITH_PORT);

        assertEquals(HttpRequestHandler.State.NotSend, mHandler.getState());
    }
}
