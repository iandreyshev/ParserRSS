package ru.iandreyshev.parserrss.models.useCase

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import ru.iandreyshev.parserrss.MocksFactory
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

@RunWith(RobolectricTestRunner::class)
class InsertRssUseCaseTest {

    companion object {
        private const val EMPTY_URL = ""
        private const val VALID_URL = "lenta.ru"
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

        createUseCase(VALID_URL).execute().get()

        verifyStartEndProcess()
        verify(mListener).rssCountIsMax()
        verifyNoMoreInteractions(mListener)
        verifyZeroInteractions(mFactory.requestHandler)
    }

    @Test
    fun callRssAlreadyExistBeforeStartRequesting() {
        whenever(mFactory.repository.isRssWithUrlExist(VALID_URL)).thenReturn(true)

        createUseCase(VALID_URL).execute().get()

        verifyStartEndProcess()
        verify(mListener).rssAlreadyExist()
        verifyNoMoreInteractions(mListener)
        verifyZeroInteractions(mFactory.requestHandler)
    }

    @Test
    fun callUrlIsEmptyBeforeStartRequesting() {
        createUseCase(EMPTY_URL).execute().get()

        verifyStartEndProcess()
        verify(mListener).urlToAddRssIsEmpty()
        verifyNoMoreInteractions(mListener)
        verifyZeroInteractions(mFactory.requestHandler)
    }

    @Test
    fun callConnectionErrorIfHandlerReturnNotSuccessValue() {
        fun verify(state: HttpRequestHandler.State) {
            whenever(mFactory.requestHandler.send(VALID_URL)).thenReturn(state)

            createUseCase(VALID_URL).execute().get()

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
        whenever(mFactory.parser.parse(EMPTY_RSS_STRING, mFactory.repository.maxArticlesInRss)).thenReturn(null)

        createUseCase(VALID_URL).execute().get()

        verifyStartEndProcess()
        verify(mListener).invalidRssFormat()
        verifyNoMoreInteractions(mListener)
    }

    @Test
    fun callSuccessAfterPutOnlyIfInsertStateIsSuccess() {
        fun verify(insertState: IRepository.InsertRssResult) {
            whenever(mFactory.repository.isFull).thenReturn(false)
            whenever(mFactory.repository.maxArticlesInRss).thenReturn(MAX_RSS_ARTICLES)
            whenever(mFactory.requestHandler.urlString).thenReturn(VALID_URL)
            whenever(mFactory.requestHandler.send(VALID_URL)).thenReturn(HttpRequestHandler.State.SUCCESS)
            whenever(mFactory.requestHandler.bodyAsString).thenReturn(EMPTY_RSS_STRING)
            whenever(mFactory.parser.parse(EMPTY_RSS_STRING, mFactory.repository.maxArticlesInRss)).thenReturn(mEmptyRss)
            whenever(mFactory.repository.putNewRss(mEmptyRss)).thenReturn(insertState)

            createUseCase(VALID_URL).execute().get()

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
                url,
                mFactory.parser,
                mFactory.articleFilter,
                mListener)
    }
}
