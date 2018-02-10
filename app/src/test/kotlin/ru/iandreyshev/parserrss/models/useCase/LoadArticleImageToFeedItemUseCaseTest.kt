package ru.iandreyshev.parserrss.models.useCase

import android.graphics.Bitmap
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import ru.iandreyshev.parserrss.MocksFactory
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

@RunWith(RobolectricTestRunner::class)
class LoadArticleImageToFeedItemUseCaseTest {

    companion object {
        private const val ARTICLE_ID = 0L
        private val IMAGE = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8)
    }

    private lateinit var mFactory: MocksFactory
    private lateinit var mListener: IUseCaseListener

    @Before
    fun setup() {
        mFactory = MocksFactory()
        mListener = mock()
    }

    @Test
    fun notCallProcessMethods() {
        whenever(mFactory.itemIcon.isUpdateStart).thenReturn(true)

        createUseCase().execute().get()

        verifyZeroInteractions(mListener)
    }

    @Test
    fun configureToViewAndReturnIfFindInRepository() {
        whenever(mFactory.itemIcon.id).thenReturn(ARTICLE_ID)
        whenever(mFactory.itemIcon.isUpdateStart).thenReturn(false)
        whenever(mFactory.repository.getArticleImageBitmapByArticleId(ARTICLE_ID)).thenReturn(IMAGE)
        whenever(mFactory.imageProps.configureToView(IMAGE)).thenReturn(IMAGE)

        createUseCase().execute().get()

        verify(mFactory.itemIcon).updateImage(IMAGE)
        verify(mFactory.imageProps).configureToView(IMAGE)
    }

    @Test
    fun notCallUpdateIfRepositoryDoNotHaveImageUrl() {
        whenever(mFactory.itemIcon.id).thenReturn(ARTICLE_ID)
        whenever(mFactory.itemIcon.isUpdateStart).thenReturn(false)
        whenever(mFactory.repository.getArticleImageBitmapByArticleId(ARTICLE_ID)).thenReturn(null)
        whenever(mFactory.repository.getArticleImageUrlByArticleId(ARTICLE_ID)).thenReturn(null)

        createUseCase().execute().get()

        verifyNoMoreInteractions(mListener)
    }

    @Test
    fun notCallUpdateIfHandlerReturnWrongStatement() {
        val imageUrl = "Image url"
        val responseBody = byteArrayOf()

        whenever(mFactory.itemIcon.id).thenReturn(ARTICLE_ID)
        whenever(mFactory.itemIcon.isUpdateStart).thenReturn(false)
        whenever(mFactory.repository.getArticleImageBitmapByArticleId(ARTICLE_ID)).thenReturn(null)
        whenever(mFactory.repository.getArticleImageUrlByArticleId(ARTICLE_ID)).thenReturn(imageUrl)
        whenever(mFactory.requestHandler.send(imageUrl)).thenReturn(HttpRequestHandler.State.SUCCESS)
        whenever(mFactory.requestHandler.body).thenReturn(responseBody)
        whenever(mFactory.imageProps.configureToMemory(any())).thenReturn(IMAGE)
        whenever(mFactory.repository.putArticleImageIfArticleExist(ARTICLE_ID, IMAGE)).thenReturn(true)
        whenever(mFactory.imageProps.configureToView(IMAGE)).thenReturn(IMAGE)

        createUseCase().execute().get()

        verify(mFactory.itemIcon).updateImage(IMAGE)
    }

    @Test
    fun notCallUpdateIfRepositoryNotContainImageAndHandlerReturnWrongResult() {
        val imageUrl = "Image url"

        whenever(mFactory.itemIcon.id).thenReturn(ARTICLE_ID)
        whenever(mFactory.itemIcon.isUpdateStart).thenReturn(false)
        whenever(mFactory.repository.getArticleImageBitmapByArticleId(ARTICLE_ID)).thenReturn(null)
        whenever(mFactory.repository.getArticleImageUrlByArticleId(ARTICLE_ID)).thenReturn(imageUrl)
        whenever(mFactory.requestHandler.send(imageUrl)).thenReturn(HttpRequestHandler.State.BAD_URL)
        whenever(mFactory.requestHandler.body).thenReturn(null)

        createUseCase().execute().get()

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