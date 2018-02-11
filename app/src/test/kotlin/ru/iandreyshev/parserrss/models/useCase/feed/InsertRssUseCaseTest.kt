package ru.iandreyshev.parserrss.models.useCase.feed

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.iandreyshev.parserrss.MocksFactory
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class InsertRssUseCaseTest {

    companion object {
        private const val EMPTY_URL = ""
        private const val VALID_URL = "valid.url"
        private const val EMPTY_RSS_STRING = ""
        private const val MAX_RSS_ARTICLES = 10
    }

    private lateinit var mEmptyRss: Rss
    private lateinit var mFactory: MocksFactory
    private lateinit var mListener: InsertRssUseCase.IListener

    @Before
    fun setup() {
        mEmptyRss = Rss()
        mFactory = MocksFactory()
        mListener = mock()
    }

    @Test
    fun callCountIsMaxIfRepositoryIsFull() {
        whenever(mFactory.repository.isFull).thenReturn(true)

        createUseCase(VALID_URL).start()

        verifyStartEndProcess()
        verify(mListener).rssCountIsMax()
        verifyNoMoreInteractions(mListener)
        verifyZeroInteractions(mFactory.requestHandler)
    }

    @Test
    fun callRssAlreadyExistBeforeStartRequesting() {
        whenever(mFactory.repository.isRssWithUrlExist(VALID_URL)).thenReturn(true)

        createUseCase(VALID_URL).start()

        verifyStartEndProcess()
        verify(mListener).rssAlreadyExist()
        verifyNoMoreInteractions(mListener)
        verifyZeroInteractions(mFactory.requestHandler)
    }

    @Test
    fun callUrlIsEmptyBeforeStartRequesting() {
        createUseCase(EMPTY_URL).start()

        verifyStartEndProcess()
        verify(mListener).urlToAddRssIsEmpty()
        verifyNoMoreInteractions(mListener)
        verifyZeroInteractions(mFactory.requestHandler)
    }

    @Test
    fun callConnectionErrorIfHandlerReturnNotSuccessValue() {
        fun verify(state: HttpRequestHandler.State) {
            whenever(mFactory.requestHandler.send(VALID_URL)).thenReturn(state)

            createUseCase(VALID_URL).start()

            verifyStartEndProcess()
            verify(mListener).connectionError(mFactory.requestHandler)
            verifyNoMoreInteractions(mListener)
            clearInvocations(mListener)
        }

        verify(HttpRequestHandler.State.NOT_SEND)
        verify(HttpRequestHandler.State.BAD_URL)
        verify(HttpRequestHandler.State.PERMISSION_DENIED)
        verify(HttpRequestHandler.State.BAD_CONNECTION)
    }

    @Test
    fun callInvalidRssFormatIfParserReturnNull() {
        whenever(mFactory.requestHandler.send(VALID_URL)).thenReturn(HttpRequestHandler.State.SUCCESS)
        whenever(mFactory.requestHandler.bodyAsString).thenReturn(EMPTY_RSS_STRING)
        whenever(mFactory.parser.parse(EMPTY_RSS_STRING, mFactory.repository.maxArticlesInRssCount)).thenReturn(null)

        createUseCase(VALID_URL).start()

        verifyStartEndProcess()
        verify(mListener).invalidRssFormat(VALID_URL)
        verifyNoMoreInteractions(mListener)
    }

    @Test
    fun callSuccessAfterPutOnlyIfInsertStateIsSuccess() {
        fun verify(insertState: IRepository.InsertRssResult) {
            whenever(mFactory.repository.isFull).thenReturn(false)
            whenever(mFactory.repository.maxArticlesInRssCount).thenReturn(MAX_RSS_ARTICLES)
            whenever(mFactory.requestHandler.urlString).thenReturn(VALID_URL)
            whenever(mFactory.requestHandler.send(VALID_URL)).thenReturn(HttpRequestHandler.State.SUCCESS)
            whenever(mFactory.requestHandler.bodyAsString).thenReturn(EMPTY_RSS_STRING)
            whenever(mFactory.parser.parse(EMPTY_RSS_STRING)).thenReturn(mEmptyRss)
            whenever(mFactory.repository.putNewRss(mEmptyRss)).thenReturn(insertState)

            createUseCase(VALID_URL).start()

            when (insertState) {
                IRepository.InsertRssResult.SUCCESS -> {
                    verify(mListener).insertNewRss(argThat { true }, eq(false))
                }
                IRepository.InsertRssResult.EXIST -> {
                    verify(mListener).rssAlreadyExist()
                }
                IRepository.InsertRssResult.FULL -> {
                    verify(mListener).rssCountIsMax()
                }
            }

            verifyStartEndProcess()
            verifyNoMoreInteractions(mListener)
            clearInvocations(mListener)
        }

        verify(IRepository.InsertRssResult.SUCCESS)
        verify(IRepository.InsertRssResult.EXIST)
        verify(IRepository.InsertRssResult.FULL)
    }

    private fun verifyStartEndProcess() {
        verify(mListener).processStart()
        verify(mListener).processEnd()
    }

    private fun createUseCase(url: String): InsertRssUseCase {
        return InsertRssUseCase(
                mFactory.repository,
                mFactory.requestHandler,
                mFactory.parser,
                url,
                mListener)
    }
}
