package ru.iandreyshev.parserrss.models.useCase.rssList

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import ru.iandreyshev.parserrss.MocksFactory
import ru.iandreyshev.parserrss.firstArgAsFun
import ru.iandreyshev.parserrss.models.rss.Rss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

class UpdateRssUseCaseTest {

    companion object {
        private const val RSS_ID = 0L
        private const val VALID_URL = "valid.url"
    }

    private lateinit var mFactory: MocksFactory
    private lateinit var mListener: UpdateRssUseCase.IListener

    @Before
    fun setup() {
        mFactory = MocksFactory()
        mListener = mock()
    }

    @Test
    fun callConnectionErrorIfHandlerReturnNotSuccess() {
        val rss = Rss(url = VALID_URL)

        fun verify(state: HttpRequestHandler.State) {
            whenever(mFactory.repository.getRssById(any())).thenReturn(rss)
            whenever(mFactory.repository.isRssWithUrlExist(VALID_URL)).thenReturn(true)
            whenever(mFactory.requestHandler.send(any())).thenReturn(state)

            createUseCase(RSS_ID).start()

            verifyProcessMethods()
            verify(mListener).connectionError(mFactory.requestHandler)
            clearInvocations(mListener)
        }

        verify(HttpRequestHandler.State.NOT_SEND)
        verify(HttpRequestHandler.State.BAD_URL)
        verify(HttpRequestHandler.State.BAD_CONNECTION)
        verify(HttpRequestHandler.State.PERMISSION_DENIED)
    }

    @Test
    fun callRssNotExistIfRepositoryNotContainThisUrlBeforeRequest() {
        whenever(mFactory.repository.isRssWithUrlExist(VALID_URL)).thenReturn(false)

        createUseCase(RSS_ID).start()

        verifyProcessMethods()
        verify(mListener).rssNotExist()
        verifyZeroInteractions(mFactory.requestHandler)
    }

    @Test
    fun callParseErrorIfParserReturnNull() {
        val rssString = ""
        val rss = Rss(url = VALID_URL)

        whenever(mFactory.repository.getRssById(any())).thenReturn(rss)
        whenever(mFactory.repository.isRssWithUrlExist(VALID_URL)).thenReturn(true)
        whenever(mFactory.requestHandler.send(VALID_URL)).thenReturn(HttpRequestHandler.State.SUCCESS)
        whenever(mFactory.requestHandler.bodyAsString).thenReturn(rssString)
        whenever(mFactory.parser.parse(rssString, mFactory.repository.maxArticlesInRssCount)).thenReturn(null)

        createUseCase(RSS_ID).start()

        verifyProcessMethods()
        verify(mListener).parseError()
    }

    @Test
    fun sortAndUpdateRssIfRepositoryContainIt() {
        val rss = Rss(url = VALID_URL)
        val rssString = ""

        whenever(mFactory.repository.getRssById(any())).thenReturn(rss)
        whenever(mFactory.repository.isRssWithUrlExist(VALID_URL)).thenReturn(true)
        whenever(mFactory.repository.updateRssWithSameUrl(rss)).thenReturn(true)
        whenever(mFactory.repository.runInTx(any())).then { it.firstArgAsFun.invoke()  }
        whenever(mFactory.requestHandler.send(VALID_URL)).thenReturn(HttpRequestHandler.State.SUCCESS)
        whenever(mFactory.requestHandler.bodyAsString).thenReturn(rssString)
        whenever(mFactory.requestHandler.sentUrlString).thenReturn(VALID_URL)
        whenever(mFactory.parser.parse(rssString)).thenReturn(rss)

        createUseCase(RSS_ID).start()

        verifyProcessMethods()
        verify(mFactory.articleFilter).sort(rss.articles)
        verify(mListener).updateRss(argThat { true })
    }

    private fun createUseCase(id: Long): UpdateRssUseCase {
        return UpdateRssUseCase(
                mFactory.repository,
                mFactory.requestHandler,
                mFactory.parser,
                id,
                mFactory.articleFilter,
                mListener
        )
    }

    private fun verifyProcessMethods() {
        verify(mListener).processStart()
        verify(mListener).processEnd()
    }
}
