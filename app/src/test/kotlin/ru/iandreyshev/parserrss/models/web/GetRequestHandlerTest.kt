package ru.iandreyshev.parserrss.models.web

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import okhttp3.*
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GetRequestHandlerTest {
    companion object {
        private const val VALID_URL_WITH_PROTOCOL = "http://domain.com/"
        private const val BAD_RESPONSE_CODE = 0
        private const val GOOD_RESPONSE_CODE = 200
        private const val MAX_BODY_SIZE = 5242880 // 5MB
    }

    private lateinit var mRequestBuilder: Request.Builder
    private lateinit var mResponseBuilder: Response.Builder
    private val mBadConnectionHandler: HttpRequestHandler
        get() {
            val request = mRequestBuilder.build()
            val response = mResponseBuilder.build()
            val client = getMockedClient(request, response)
            whenever(client.newCall(request)).thenThrow(Exception::class.java)
            return getHandler(client, request)
        }

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
    fun returnNotSendAfterCreate() {
        val handler = GetRequestHandler()
        assertEquals(HttpRequestHandler.State.NOT_SEND, handler.state)
    }

    @Test
    fun returnEmptyUrlStringAfterCreate() {
        val handler = GetRequestHandler()
        assertEquals("", handler.urlString)
    }

    @Test
    fun returnNullBodyBeforeSendRequest() {
        val handler = GetRequestHandler()
        assertNull(handler.body)
        assertNull(handler.bodyAsString)
    }

    @Test
    fun returnBadConnectionStateIfCodeNotGood() {
        val request = mRequestBuilder.build()
        val response = mResponseBuilder
                .code(BAD_RESPONSE_CODE)
                .build()
        val handler = getHandler(getMockedClient(request, response), request)

        assertEquals(HttpRequestHandler.State.BAD_CONNECTION, handler.send(VALID_URL_WITH_PROTOCOL))
    }

    @Test
    fun returnBadConnectionStateIfResponseBodyIsNull() {
        val request = mRequestBuilder.build()
        val response = mResponseBuilder
                .body(null)
                .build()
        val handler = getHandler(getMockedClient(request, response), request)

        assertEquals(HttpRequestHandler.State.BAD_CONNECTION, handler.send(VALID_URL_WITH_PROTOCOL))
    }

    @Test
    fun returnBadConnectionStateIfResponseBodyMoreThanMaxBytesCount() {
        val maxBytesCount = 0L
        val bodyBytesCount = 16L
        val request = mRequestBuilder.build()
        val response = mResponseBuilder
                .body(ResponseBody.create(null, bodyBytesCount, mock()))
                .build()
        val handler = getHandler(getMockedClient(request, response), request)
        handler.maxContentBytes = maxBytesCount

        assertEquals(HttpRequestHandler.State.BAD_CONNECTION, handler.send(VALID_URL_WITH_PROTOCOL))
    }

    @Test
    fun returnPermissionDeniedStateIfCatchSecurityException() {
        val request = mRequestBuilder.build()
        val response = mResponseBuilder.build()
        val client = getMockedClient(request, response)
        whenever(client.newCall(request)).thenThrow(SecurityException::class.java)
        val handler = getHandler(client, request)

        assertEquals(HttpRequestHandler.State.PERMISSION_DENIED, handler.send(VALID_URL_WITH_PROTOCOL))
    }

    @Test
    fun returnBadConnectionIfResponseBodyMoreThanMax() {
        val request = mRequestBuilder.build()
        val response = mResponseBuilder
                .body(ResponseBody.create(null, ByteArray(MAX_BODY_SIZE + 1)))
                .build()
        val handler = getHandler(getMockedClient(request, response), request)

        assertEquals(HttpRequestHandler.State.BAD_CONNECTION, handler.send(VALID_URL_WITH_PROTOCOL))
    }

    @Test
    fun returnSuccessStateIfBodyIsValid() {
        val request = mRequestBuilder.build()
        val response = mResponseBuilder
                .body(ResponseBody.create(null, ByteArray(0)))
                .build()
        val handler = getHandler(getMockedClient(request, response), request)

        assertEquals(HttpRequestHandler.State.SUCCESS, handler.send(VALID_URL_WITH_PROTOCOL))
    }

    @Test
    fun trimUrlStringAfterSend() {
        val url = "http://url.com"
        val notTrimUrl = "   $url   "
        val handler = mBadConnectionHandler
        handler.send(notTrimUrl)

        assertEquals(url, handler.urlString)
    }

    @Test
    fun removeBackSlashFromUrlAfterSend() {
        val urlWithoutEndSlash = "http://valid.url/path"
        val handler = mBadConnectionHandler
        handler.send(urlWithoutEndSlash + "/")

        assertEquals(urlWithoutEndSlash, handler.urlString)
    }

    @Test
    fun urlsWithRelativePartsEquals() {
        val firstHandler = mBadConnectionHandler
        val secondHandler = mBadConnectionHandler

        firstHandler.send("domain.com/zero/first/..")
        secondHandler.send("domain.com/zero")
        assertEquals(firstHandler.urlString, secondHandler.urlString)

        firstHandler.send("domain.com/zero/first/../")
        secondHandler.send("domain.com/zero")
        assertEquals(firstHandler.urlString, secondHandler.urlString)

        firstHandler.send("domain.com/zero/first/..")
        secondHandler.send("domain.com/zero/")
        assertEquals(firstHandler.urlString, secondHandler.urlString)

        firstHandler.send("domain.com/zero/first/../")
        secondHandler.send("domain.com/zero/")
        assertEquals(firstHandler.urlString, secondHandler.urlString)
    }

    @Test
    fun replaceBackSlashesInUrlAfterSend() {
        val handler = mBadConnectionHandler
        handler.send("http://valid.url\\path\\")

        assertEquals("http://valid.url/path", handler.urlString)
    }

    private fun getMockedClient(request: Request, response: Response): OkHttpClient {
        val call: Call = mock()
        whenever(call.execute()).thenReturn(response)

        val client: OkHttpClient = mock()
        whenever(client.newCall(request)).thenReturn(call)

        return client
    }

    private fun getHandler(client: OkHttpClient, request: Request): HttpRequestHandler {
        return object : HttpRequestHandler() {
            override fun createClient(clientBuilder: OkHttpClient.Builder): OkHttpClient {
                return client
            }

            override fun createRequest(url: HttpUrl): Request {
                return request
            }
        }
    }
}
