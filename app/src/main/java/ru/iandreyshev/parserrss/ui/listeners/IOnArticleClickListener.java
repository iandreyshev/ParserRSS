package ru.iandreyshev.parserrss.ui.listeners;

import ru.iandreyshev.parserrss.models.rss.IViewRssArticle;

public interface IOnArticleClickListener {
    void onArticleClick(final IViewRssArticle article);
}
