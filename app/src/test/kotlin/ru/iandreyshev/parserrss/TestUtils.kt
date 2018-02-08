package ru.iandreyshev.parserrss

import org.apache.commons.io.IOUtils
import org.mockito.Mockito
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

object TestUtils {
    fun readFromFile(filePath: String): String? {
        return try {
            IOUtils.toString(
                    TestUtils::class.java.getResourceAsStream(filePath),
                    "UTF-8"
            )
        } catch (ex: Exception) {
            null
        }
    }
}

class MocksFactory {
    val repository = Mockito.mock(IRepository::class.java)
    val requestHandler = Mockito.mock(HttpRequestHandler::class.java)
    val articleFilter = Mockito.mock(IArticlesFilter::class.java)
}
