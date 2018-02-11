package ru.iandreyshev.parserrss.models.useCase.rssList

import android.graphics.Bitmap
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.iandreyshev.parserrss.MocksFactory
import ru.iandreyshev.parserrss.models.repository.Article
import ru.iandreyshev.parserrss.models.repository.ArticleImage
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class LoadArticleImageToFeedItemUseCaseTest {

    companion object {
        private const val ARTICLE_ID = 0L
        private val IMAGE_BITMAP = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8)
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
        val image = ArticleImage(bitmap = IMAGE_BITMAP)

        whenever(mFactory.itemIcon.id).thenReturn(ARTICLE_ID)
        whenever(mFactory.repository.getArticleImageByArticleId(ARTICLE_ID)).thenReturn(image)
        whenever(mFactory.imageProps.configureToView(any())).thenReturn(IMAGE_BITMAP)

        createUseCase().start()

        verify(mFactory.imageProps).configureToView(IMAGE_BITMAP)
        verify(mListener).insertImage(mFactory.itemIcon, mFactory.itemIcon.id, IMAGE_BITMAP)
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