package ru.iandreyshev.parserrss.models.useCase.feed

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class OpenRssInfoUseCaseTest {
    @Test
    fun openInfoById() {
        val rssId = 0L
        val listener: OpenArticleUseCase.IListener = mock()

        OpenArticleUseCase(rssId, listener)
                .start()

        verify(listener).processStart()
        verify(listener).processEnd()
        verify(listener).openArticle(rssId)
    }
}
