package ru.iandreyshev.parserrss.ui.adapter;

import ru.iandreyshev.parserrss.models.rss.RssArticle;

public interface IOnArticleClickListener {
    void onItemClick(RssArticle article);
}
