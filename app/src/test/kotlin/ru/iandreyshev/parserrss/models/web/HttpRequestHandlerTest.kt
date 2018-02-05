package ru.iandreyshev.parserrss.models.web

import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HttpRequestHandlerTest {
    companion object {
        private const val VALID_URL_WITH_PROTOCOL = "http://domain.com"
        private const val VALID_URL_WITHOUT_PROTOCOL = "domain.com"
        private const val INVALID_URL = ""
    }

    @Test
    fun returnNotSendAfterCreateWithValidUrl() {
        val handler = HttpRequestHandler(VALID_URL_WITH_PROTOCOL)
        assertEquals(handler.state, HttpRequestHandler.State.NOT_SEND)
    }

    @Test
    fun returnNotSendAfterCreateWithValidUrlWithoutProtocol() {
        val handler = HttpRequestHandler(VALID_URL_WITHOUT_PROTOCOL)
        assertEquals(handler.state, HttpRequestHandler.State.NOT_SEND)
    }

    @Test
    fun returnBadUrlAfterCreateWithInvalidUrl() {
        val handler = HttpRequestHandler(INVALID_URL)
        assertEquals(HttpRequestHandler.State.NOT_SEND, handler.state)
    }

    @Test
    fun returnUrlStringAfterCreate() {
        val handler = HttpRequestHandler(VALID_URL_WITH_PROTOCOL)
        assertEquals(handler.urlString, VALID_URL_WITH_PROTOCOL)
    }

    @Test
    fun returnNullBodyBeforeSendRequest() {
        val handler = HttpRequestHandler(VALID_URL_WITH_PROTOCOL)
        assertNull(handler.body)
        assertNull(handler.bodyAsString)
    }

    @Test
    fun resetStateAfterResetUrl() {
        val handler = HttpRequestHandler(VALID_URL_WITH_PROTOCOL)
        assertEquals(handler.state, HttpRequestHandler.State.NOT_SEND)
        handler.sendGet(INVALID_URL)
        assertEquals(handler.state, HttpRequestHandler.State.BAD_URL)
    }
}
