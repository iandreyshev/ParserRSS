package ru.iandreyshev.parserrss.models.useCase.article

import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.iandreyshev.parserrss.MocksFactory
import ru.iandreyshev.parserrss.models.repository.Article

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
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
        val validUrl = "article.url"
        val article = Article(originUrl = validUrl)

        whenever(mFactory.repository.getArticleById(ARTICLE_ID)).thenReturn(article)

        createUseCase().start()

        verify(mListener).openOriginal(argThat { path.toString() == validUrl })
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
