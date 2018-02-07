package ru.iandreyshev.parserrss.models.useCase

import io.objectbox.BoxStore
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.repository.MyObjectBox
import java.io.File

@RunWith(RobolectricTestRunner::class)
class LoadArticleUseCaseTest {

    companion object {
        private lateinit var mStore: BoxStore
    }

    @Before
    fun setup() {
        val tempFile = File.createTempFile("object-store-test", "")
        assertTrue(tempFile.delete())
        mStore = MyObjectBox.builder().directory(tempFile).build()
    }

    @Test
    fun callFailIfRepositoryReturnNull() {
        val repo = mock(IRepository::class.java)
        val listener = mock(LoadArticleUseCase.IListener::class.java)
        Mockito.`when`(repo.getArticleById(1)).thenReturn(null)
    }
}
