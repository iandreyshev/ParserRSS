package ru.iandreyshev.parserrss.models.web;

import org.junit.Test;

import static org.junit.Assert.*;

public class HttpRequestHandlerTest {
    @Test
    public void returnInvalidUrlStateAfterSendIfUrlIsNull() {
        HttpRequestHandler requestHandler = new HttpRequestHandler();

        final String nullUrl = null;

        requestHandler.sendGet(nullUrl);

        assertEquals(requestHandler.getState(), HttpRequestHandler.State.BadUrl);

        final Url url = null;

        requestHandler.sendGet(url);

        assertEquals(requestHandler.getState(), HttpRequestHandler.State.BadUrl);
    }
}