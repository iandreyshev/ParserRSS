package ru.iandreyshev.parserrss.factory.useCase

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener

@RunWith(RobolectricTestRunner::class)
class UseCaseFactoryTest {

    @Test
    fun verifyUseCasesTypes() {

    }

    private fun <T> verify(type: UseCaseType, listener: IUseCaseListener, data: Any) {
    }
}