package ru.iandreyshev.parserrss.models.useCase.rssList

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import ru.iandreyshev.parserrss.MocksFactory
import ru.iandreyshev.parserrss.models.rss.Article
import ru.iandreyshev.parserrss.models.rss.ArticleImage
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

class LoadArticleImageToFeedItemUseCaseTest {

    companion object {
        private const val ARTICLE_ID = 0L
    }

    private lateinit var mFactory: MocksFactory
    private lateinit var mListener: LoadArticleImageToFeedItemUseCase.IListener

    @Before
    fun setup() {
        mFactory = MocksFactory()
        mListener = mock()
    }

    @Test
    fun notCallProcessMethods() {
        createUseCase().start()

        verifyZeroInteractions(mListener)
    }

    @Test
    fun configureToViewAndReturnIfFindInRepository() {
        val imageBytes = byteArrayOf()
        val image = ArticleImage(
                articleId = ARTICLE_ID,
                bytes = imageBytes
        )

        whenever(mFactory.itemIcon.id).thenReturn(ARTICLE_ID)
        whenever(mFactory.repository.getArticleImageByArticleId(ARTICLE_ID)).thenReturn(image)
        whenever(mFactory.imageProps.configureToView(any())).thenReturn(imageBytes)

        createUseCase().start()

        verify(mFactory.imageProps).configureToView(imageBytes)
        verify(mListener).insertImage(mFactory.itemIcon, mFactory.itemIcon.id, imageBytes)
    }

    @Test
    fun notCallUpdateIfRepositoryDoNotHaveImageUrl() {
        whenever(mFactory.itemIcon.id).thenReturn(ARTICLE_ID)
        whenever(mFactory.repository.getArticleImageByArticleId(ARTICLE_ID)).thenReturn(null)
        whenever(mFactory.repository.getArticleById(ARTICLE_ID)).thenReturn(null)

        createUseCase().start()

        verifyNoMoreInteractions(mListener)
    }

    @Test
    fun notCallUpdateIfHandlerReturnWrongStatement() {
        whenever(mFactory.itemIcon.id).thenReturn(ARTICLE_ID)
        whenever(mFactory.repository.getArticleImageByArticleId(ARTICLE_ID)).thenReturn(null)
        whenever(mFactory.repository.getArticleById(ARTICLE_ID)).thenReturn(Article())
        whenever(mFactory.requestHandler.send(any())).thenReturn(HttpRequestHandler.State.BAD_CONNECTION)

        createUseCase().start()

        verify(mFactory.itemIcon, times(2)).id
        verifyNoMoreInteractions(mFactory.itemIcon)
    }

    @Test
    fun notCallUpdateIfRepositoryNotContainImageAndHandlerReturnWrongResult() {
        whenever(mFactory.itemIcon.id).thenReturn(ARTICLE_ID)
        whenever(mFactory.repository.getArticleImageByArticleId(ARTICLE_ID)).thenReturn(null)
        whenever(mFactory.repository.getArticleById(ARTICLE_ID)).thenReturn(Article())
        whenever(mFactory.requestHandler.send(any())).thenReturn(HttpRequestHandler.State.BAD_URL)
        whenever(mFactory.requestHandler.body).thenReturn(null)

        createUseCase().start()

        verify(mFactory.itemIcon, times(2)).id
        verifyNoMoreInteractions(mFactory.itemIcon)
    }

    private fun createUseCase(): LoadArticleImageToFeedItemUseCase {
        return LoadArticleImageToFeedItemUseCase(
                mFactory.repository,
                mFactory.requestHandler,
                mFactory.imageProps,
                mFactory.itemIcon,
                mListener
        )
    }
}