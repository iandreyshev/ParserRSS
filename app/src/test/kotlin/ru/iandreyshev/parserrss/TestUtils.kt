package ru.iandreyshev.parserrss

import com.nhaarman.mockito_kotlin.mock
import org.apache.commons.io.IOUtils
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.parser.RssParser
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
    val repository: IRepository = mock()
    val requestHandler: HttpRequestHandler = mock()
    val articleFilter: IArticlesFilter = mock()
    val parser: RssParser = mock()
}
