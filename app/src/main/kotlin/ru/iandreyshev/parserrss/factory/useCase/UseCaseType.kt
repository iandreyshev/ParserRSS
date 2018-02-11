package ru.iandreyshev.parserrss.factory.useCase

enum class UseCaseType {
    // Feed activity
    FEED_INSERT_RSS,
    FEED_UPDATE_RSS,
    FEED_DELETE_RSS,
    FEED_LOAD_ALL_RSS,
    FEED_OPEN_RSS_INFO,

    // Article activity
    ARTICLE_LOAD_DATA,
    ARTICLE_LOAD_IMAGE,
    ARTICLE_OPEN_ORIGINAL,

    // Rss page
    RSS_PAGE_LOAD_ARTICLES_LIST,
    RSS_PAGE_LOAD_ICON,
    RSS_PAGE_OPEN_ARTICLE,

    // Rss info dialog
    RSS_INFO_LOAD_DATA,
    RSS_INFO_OPEN_ORIGINAL
}
