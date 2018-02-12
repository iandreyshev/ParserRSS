package ru.iandreyshev.parserrss.models.useCase.article

import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import ru.iandreyshev.parserrss.MocksFactory
import ru.iandreyshev.parserrss.models.extention.uri
import ru.iandreyshev.parserrss.models.rss.Article

class OpenArticleOriginalUseCaseTest {

    companion object {
        private const val ARTICLE_ID = 0L
    }

    private lateinit var mFactory: MocksFactory
    private lateinit var mListener: OpenArticleOriginalUseCase.IListener

    @Before
    fun setup() {
        mFactory = MocksFactory()
        mListener = mock()
    }

    @Test
    fun callProcessMethods() {
        createUseCase().start()

        verify(mListener).processStart()
        verify(mListener).processEnd()
    }

    @Test
    fun returnArticleUrlIfFound() {
        val article = Article(originUrl = "http://article.url/path")

        whenever(mFactory.repository.getArticleById(ARTICLE_ID)).thenReturn(article)

        createUseCase().start()

        verify(mListener).openOriginal(argThat { this == article.originUrl })
    }

    @Test
    fun returnNullIfArticleNotFound() {
        whenever(mFactory.repository.getArticleById(ARTICLE_ID)).thenReturn(null)

        createUseCase().start()

        verify(mListener).openOriginal(null)
    }

    private fun createUseCase(): OpenArticleOriginalUseCase {
        return OpenArticleOriginalUseCase(
                mFactory.repository,
                ARTICLE_ID,
                mListener
        )
    }
}
