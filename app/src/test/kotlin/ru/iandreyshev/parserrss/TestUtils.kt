package ru.iandreyshev.parserrss

import org.apache.commons.io.IOUtils

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
