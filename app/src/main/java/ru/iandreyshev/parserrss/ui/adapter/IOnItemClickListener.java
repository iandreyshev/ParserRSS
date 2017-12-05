package ru.iandreyshev.parserrss.ui.adapter;

import android.view.View;

import ru.iandreyshev.parserrss.models.article.IArticleInfo;

public interface IOnItemClickListener<T> {
    void onItemClick(View view, T item);
}
