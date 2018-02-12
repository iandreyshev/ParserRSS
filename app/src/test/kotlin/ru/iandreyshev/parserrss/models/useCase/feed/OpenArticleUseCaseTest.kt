package ru.iandreyshev.parserrss.models.useCase.feed

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class OpenArticleUseCaseTest {
    @Test
    fun openArticleById() {
        val articleId = 0L
        val listener: OpenArticleUseCase.IListener = mock()

        OpenArticleUseCase(articleId, listener)
                .start()

        verify(listener).processStart()
        verify(listener).processEnd()
        verify(listener).openArticle(articleId)
    }
}