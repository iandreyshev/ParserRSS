package ru.iandreyshev.parserrss.ui.extention

private const val CUT_TAB_TITLE_PATTERN = "%s..."
private const val MAX_TAB_TITLE_LINE_LENGTH = 16

internal val String.tabTitle: String
    get() {
        var result = this.trim()
        val firstSpacePos = result.indexOf(" ")
        var maxLength = MAX_TAB_TITLE_LINE_LENGTH

        if (firstSpacePos < MAX_TAB_TITLE_LINE_LENGTH) {
            result = result.replaceFirst(" ", "\n")
            maxLength += firstSpacePos
        }

        if (result.length > maxLength) {
            result = result.substring(0, maxLength - 1)
            result = String.format(CUT_TAB_TITLE_PATTERN, result)
        }

        return result
    }
