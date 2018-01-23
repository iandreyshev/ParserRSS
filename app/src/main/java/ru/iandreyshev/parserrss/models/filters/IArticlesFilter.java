package ru.iandreyshev.parserrss.models.filters;

import android.support.annotation.NonNull;

import java.util.List;

import ru.iandreyshev.parserrss.models.repository.Article;

public interface IArticlesFilter {
    @NonNull
    List<Article> sort(@NonNull final List<Article> articles);
}
