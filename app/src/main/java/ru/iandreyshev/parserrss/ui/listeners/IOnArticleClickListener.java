package ru.iandreyshev.parserrss.ui.listeners;

import ru.iandreyshev.parserrss.models.rss.RssArticle;

public interface IOnArticleClickListener {
    void onArticleClick(final RssArticle article);
}
