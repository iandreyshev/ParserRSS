package ru.iandreyshev.parserrss.ui.adapter;

import ru.iandreyshev.parserrss.models.rss.IRssArticle;

public interface IOnArticleClickListener {
    void onItemClick(IRssArticle article);
}
