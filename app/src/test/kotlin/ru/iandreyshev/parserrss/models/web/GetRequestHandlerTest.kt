package ru.iandreyshev.parserrss.models.web

import okhttp3.*
import okio.BufferedSource
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GetRequestHandlerTest {
    companion object {
        private const val VALID_URL_WITH_PROTOCOL = "http://domain.com"
        private const val VALID_URL_WITHOUT_PROTOCOL = "domain.com"
        private const val INVALID_URL = ""
        private const val BAD_RESPONSE_CODE = 0
        private const val GOOD_RESPONSE_CODE = 200
        private const val MAX_BODY_SIZE = 5242880 // 5MB
    }

    private lateinit var mRequestBuilder: Request.Builder
    private lateinit var mResponseBuilder: Response.Builder

    @Before
    fun setup() {
        mRequestBuilder = Request.Builder().url(VALID_URL_WITH_PROTOCOL)
        mResponseBuilder = Response.Builder()
                .message("")
                .code(GOOD_RESPONSE_CODE)
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(null, "content"))
                .request(mRequestBuilder.build())
    }

    @Test
    fun returnNotSendAfterCreateWithValidUrl() {
        val handler = GetRequestHandler(VALID_URL_WITH_PROTOCOL)
        assertEquals(HttpRequestHandler.State.NOT_SEND, handler.state)
    }

    @Test
    fun returnNotSendAfterCreateWithValidUrlWithoutProtocol() {
        val handler = GetRequestHandler(VALID_URL_WITHOUT_PROTOCOL)
        assertEquals(HttpRequestHandler.State.NOT_SEND, handler.state)
    }

    @Test
    fun returnBadUrlAfterCreateWithInvalidUrl() {
        val handler = GetRequestHandler(INVALID_URL)
        assertEquals(handler.state, HttpRequestHandler.State.NOT_SEND)
    }

    @Test
    fun returnUrlStringAfterCreate() {
        val handler = GetRequestHandler(VALID_URL_WITH_PROTOCOL)
        assertEquals(VALID_URL_WITH_PROTOCOL, handler.urlString)
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
        assertEquals(HttpRequestHandler.State.NOT_SEND, handler.state)
        handler.send(INVALID_URL)
        assertEquals(HttpRequestHandler.State.BAD_URL, handler.state)
    }

    @Test
    fun returnBadConnectionStateIfCodeNotGood() {
        val request = mRequestBuilder.build()
        val response = mResponseBuilder
                .code(BAD_RESPONSE_CODE)
                .build()
        val handler = getHandler(getMockedClient(request, response), request)

        assertEquals(HttpRequestHandler.State.BAD_CONNECTION, handler.send())
    }

    @Test
    fun returnBadConnectionStateIfResponseBodyIsNull() {
        val request = mRequestBuilder.build()
        val response = mResponseBuilder
                .body(null)
                .build()
        val handler = getHandler(getMockedClient(request, response), request)

        assertEquals(HttpRequestHandler.State.BAD_CONNECTION, handler.send())
    }

    @Test
    fun returnBadConnectionStateIfResponseBodyMoreThanMaxBytesCount() {
        val maxBytesCount = 0L
        val bodyBytesCount = 16L
        val request = mRequestBuilder.build()
        val response = mResponseBuilder
                .body(ResponseBody.create(null, bodyBytesCount, mock(BufferedSource::class.java)))
                .build()
        val handler = getHandler(getMockedClient(request, response), request)
        handler.maxContentBytes = maxBytesCount

        assertEquals(HttpRequestHandler.State.BAD_CONNECTION, handler.send())
    }

    @Test
    fun returnPermissionDeniedStateIfCatchSecurityException() {
        val request = mRequestBuilder.build()
        val response = mResponseBuilder.build()
        val client = getMockedClient(request, response)
        `when`(client.newCall(request)).thenThrow(SecurityException::class.java)
        val handler = getHandler(client, request)

        assertEquals(HttpRequestHandler.State.PERMISSION_DENIED, handler.send())
    }

    @Test
    fun returnBadConnectionIfResponseBodyMoreThanMax() {
        val request = mRequestBuilder.build()
        val response = mResponseBuilder
                .body(ResponseBody.create(null, ByteArray(MAX_BODY_SIZE + 1)))
                .build()
        val handler = getHandler(getMockedClient(request, response), request)

        assertEquals(HttpRequestHandler.State.BAD_CONNECTION, handler.send())
    }

    @Test
    fun returnSuccessStateIfBodyIsValid() {
        val request = mRequestBuilder.build()
        val response = mResponseBuilder
                .body(ResponseBody.create(null, ByteArray(0)))
                .build()
        val handler = getHandler(getMockedClient(request, response), request)

        assertEquals(HttpRequestHandler.State.SUCCESS, handler.send())
    }

    private fun getMockedClient(request: Request, response: Response): OkHttpClient {
        val call = mock(Call::class.java)
        `when`(call.execute()).thenReturn(response)

        val client = mock(OkHttpClient::class.java)
        `when`(client.newCall(request)).thenReturn(call)

        return client
    }

    private fun getHandler(client: OkHttpClient, request: Request): HttpRequestHandler {
        return object : HttpRequestHandler(VALID_URL_WITH_PROTOCOL) {
            override fun createClient(clientBuilder: OkHttpClient.Builder): OkHttpClient {
                return client
            }

            override fun createRequest(url: HttpUrl): Request {
                return request
            }
        }
    }
}
