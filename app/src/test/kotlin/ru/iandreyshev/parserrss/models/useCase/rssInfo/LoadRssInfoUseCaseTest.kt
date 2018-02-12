package ru.iandreyshev.parserrss.models.useCase.rssInfo

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import ru.iandreyshev.parserrss.models.rss.Rss

class LoadRssInfoUseCaseTest {

    @Test
    fun callLoadDataIfRssNotNull() {
        val title = "Title"
        val description = "Description"
        val rss = Rss(title = title, description = description)
        val listener: LoadRssInfoUseCase.IListener = mock()

        LoadRssInfoUseCase(rss, listener)
                .start()

        verify(listener).processStart()
        verify(listener).processEnd()
        verify(listener).enableOpenOriginalButton(false)
        verify(listener).loadData(rss.title, rss.description)
    }

    @Test
    fun callLoadDataIfRssIsNull() {
        val listener: LoadRssInfoUseCase.IListener = mock()
        LoadRssInfoUseCase(null, listener)
                .start()

        verify(listener).processStart()
        verify(listener).processEnd()
        verify(listener).enableOpenOriginalButton(false)
        verify(listener).loadData(null, null)
    }
}