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
        val handler = GetRequestHandler(VALID_URL_WITH_PROTOCOL)
        assertEquals(handler.state, GetRequestHandler.State.NOT_SEND)
    }

    @Test
    fun returnNotSendAfterCreateWithValidUrlWithoutProtocol() {
        val handler = GetRequestHandler(VALID_URL_WITHOUT_PROTOCOL)
        assertEquals(handler.state, GetRequestHandler.State.NOT_SEND)
    }

    @Test
    fun returnBadUrlAfterCreateWithInvalidUrl() {
        val handler = GetRequestHandler(INVALID_URL)
        assertEquals(GetRequestHandler.State.NOT_SEND, handler.state)
    }

    @Test
    fun returnUrlStringAfterCreate() {
        val handler = GetRequestHandler(VALID_URL_WITH_PROTOCOL)
        assertEquals(handler.urlString, VALID_URL_WITH_PROTOCOL)
    }

    @Test
    fun returnNullBodyBeforeSendRequest() {
        val handler = GetRequestHandler(VALID_URL_WITH_PROTOCOL)
        assertNull(handler.body)
        assertNull(handler.bodyAsString)
    }

    @Test
    fun resetStateAfterResetUrl() {
        val handler = GetRequestHandler(VALID_URL_WITH_PROTOCOL)
        assertEquals(handler.state, GetRequestHandler.State.NOT_SEND)
        handler.sendGet(INVALID_URL)
        assertEquals(handler.state, GetRequestHandler.State.BAD_URL)
    }
}
