package ru.iandreyshev.parserrss.models.useCase

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import ru.iandreyshev.parserrss.MocksFactory
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.viewModels.ViewArticle
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

@RunWith(RobolectricTestRunner::class)
class UpdateRssUseCaseTest {

    companion object {
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
        fun verify(state: HttpRequestHandler.State) {
            whenever(mFactory.repository.isRssWithUrlExist(VALID_URL)).thenReturn(true)
            whenever(mFactory.requestHandler.send()).thenReturn(state)

            createUseCase(VALID_URL).execute().get()

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

        createUseCase(VALID_URL).execute().get()

        verifyProcessMethods()
        verify(mListener).rssNotExist()
        verifyZeroInteractions(mFactory.requestHandler)
    }

    @Test
    fun callParseErrorIfParserReturnNull() {
        val rssString = ""

        whenever(mFactory.repository.isRssWithUrlExist(VALID_URL)).thenReturn(true)
        whenever(mFactory.requestHandler.send(VALID_URL)).thenReturn(HttpRequestHandler.State.SUCCESS)
        whenever(mFactory.requestHandler.bodyAsString).thenReturn(rssString)
        whenever(mFactory.parser.parse(rssString, mFactory.repository.maxArticlesInRssCount)).thenReturn(null)

        createUseCase(VALID_URL).execute().get()

        verifyProcessMethods()
        verify(mListener).parseError()
    }

    @Test
    fun sortAndUpdateRssIfRepositorySaveIt() {
        val rss = Rss()
        val rssString = ""

        whenever(mFactory.repository.isRssWithUrlExist(VALID_URL)).thenReturn(true)
        whenever(mFactory.repository.updateRssWithSameUrl(rss)).thenReturn(true)
        whenever(mFactory.requestHandler.send(VALID_URL)).thenReturn(HttpRequestHandler.State.SUCCESS)
        whenever(mFactory.requestHandler.bodyAsString).thenReturn(rssString)
        whenever(mFactory.requestHandler.urlString).thenReturn(VALID_URL)
        whenever(mFactory.parser.parse(rssString, mFactory.repository.maxArticlesInRssCount)).thenReturn(rss)

        createUseCase(VALID_URL).execute().get()

        verifyProcessMethods()
        verify(mFactory.articleFilter).sort(rss.articles)
        verify(mListener).updateRss(argThat {
            var isEqual = true
            forEachIndexed { index: Int, article: ViewArticle ->
                if (rss.articles[index].id != article.id) {
                    isEqual = false
                }
            }
            return@argThat isEqual
        })
    }

    private fun createUseCase(url: String): UpdateRssUseCase {
        return UpdateRssUseCase(
                mFactory.repository,
                mFactory.requestHandler,
                mFactory.parser,
                url,
                mFactory.articleFilter,
                mListener
        )
    }

    private fun verifyProcessMethods() {
        verify(mListener).processStart()
        verify(mListener).processEnd()
    }
}
