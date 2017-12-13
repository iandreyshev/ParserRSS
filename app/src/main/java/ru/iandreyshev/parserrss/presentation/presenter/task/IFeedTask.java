package ru.iandreyshev.parserrss.presentation.presenter.task;

import java.util.List;

import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.feed.Feed;

public interface IFeedTask {
    List<Article> getArticles();

    Feed getFeed();
}
