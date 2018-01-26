package ru.iandreyshev.parserrss.models.web

import org.junit.Test

import org.junit.Assert.*

class HttpRequestHandlerTest {
    companion object {
        private const val VALID_URL = "http://domain.com"
        private const val URL_WITHOUT_PROTOCOL = "domain.com"
        private const val URL_WITH_PORT = "http://domain.com:80/"
    }

    private lateinit var handler: HttpRequestHandler

    @Test
    fun returnNotSendStateAfterCreate() {
        handler = HttpRequestHandler(VALID_URL)

        assertEquals(HttpRequestHandler.State.NotSend, handler.state)
    }

    @Test
    fun returnNotSendAfterInitWithUrlWithoutProtocol() {
        handler = HttpRequestHandler(URL_WITHOUT_PROTOCOL)

        assertEquals(handler.state, HttpRequestHandler.State.NotSend)
    }

    @Test
    fun returnNotSendAfterInitFromUrlWithPort() {
        handler = HttpRequestHandler(URL_WITH_PORT)

        assertEquals(HttpRequestHandler.State.NotSend, handler.state)
    }
}
