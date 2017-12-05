package ru.iandreyshev.parserrss.presentation.presenter.feed.refreshTask;

import java.util.List;

import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;

public interface IOnSuccessListener {
    void onSuccessEvent(IFeedInfo feed, List<IArticleInfo> result);
}
