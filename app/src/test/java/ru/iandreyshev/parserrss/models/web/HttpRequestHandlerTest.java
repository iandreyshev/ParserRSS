package ru.iandreyshev.parserrss.models.web;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpRequestHandlerTest {
    private HttpRequestHandler mHandler;

    @Before
    public void reset() {
        mHandler = new HttpRequestHandler();
    }

    @Test
    public void return_invalid_url_state_after_send_with_null_url() {
        final String nullUrl = null;
        mHandler.sendGet(nullUrl);

        assertEquals(HttpRequestHandler.State.InvalidUrl, mHandler.getState());

        final Url url = null;
        mHandler.sendGet(url);

        assertEquals(HttpRequestHandler.State.InvalidUrl, mHandler.getState());
    }

    @Test
    public void return_not_send_state_after_create() {
        assertEquals(HttpRequestHandler.State.NotSend, mHandler.getState());
    }
}
