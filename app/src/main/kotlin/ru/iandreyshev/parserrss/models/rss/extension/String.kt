package ru.iandreyshev.parserrss.models.rss.extension

import org.jdom2.Document
import org.jdom2.input.SAXBuilder
import java.io.StringReader
import java.util.ArrayList

private object DocBuilders {
    private const val DISABLE_DTD_FEATURE = "http://apache.org/xml/features/nonValidating/load-external-dtd"
    val BUILDERS = ArrayList<SAXBuilder>()

    private val withoutDtdBuilder = SAXBuilder()

    init {
        withoutDtdBuilder.setFeature(DISABLE_DTD_FEATURE, false)
        BUILDERS.add(withoutDtdBuilder)
        BUILDERS.add(SAXBuilder())
    }
}

internal fun String.toDocument(): Document? {
    for (builder in DocBuilders.BUILDERS) {
        try {
            return builder.build(StringReader(this)) ?: continue
        } catch (ex: Exception) {
            continue
        }
    }

    return null
}
