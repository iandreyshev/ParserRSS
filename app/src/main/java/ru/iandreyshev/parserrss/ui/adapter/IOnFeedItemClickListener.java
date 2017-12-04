package ru.iandreyshev.parserrss.ui.adapter;

import android.view.View;

import ru.iandreyshev.parserrss.models.article.IArticleInfo;

public interface IOnFeedItemClickListener {
    void onItemClick(View view, IArticleInfo item);
}
