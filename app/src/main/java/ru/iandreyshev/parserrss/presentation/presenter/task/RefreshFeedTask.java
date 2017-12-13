package ru.iandreyshev.parserrss.presentation.presenter.task;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.app.FeedLoader;
import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.feed.Feed;

public class RefreshFeedTask
        extends Task<Feed, Void, IFeedTask, RefreshFeedTask.ErrorStatus>
        implements IFeedTask {

    private Feed mFeed;
    private List<Article> mArticles = new ArrayList<>();

    @Override
    public List<Article> getArticles() {
        return mArticles;
    }

    @Override
    public Feed getFeed() {
        return mFeed;
    }

    public enum ErrorStatus {
        InternetPermissionDenied,
        BadConnection,
        InvalidFormat,
    }

    @Override
    protected RefreshFeedTask doInBackground(Feed... feedsCollection) {
        mFeed = feedsCollection[0];

        FeedLoader loader = new FeedLoader();
        loader.load(mFeed.getUrl());

        if (loader.getState() != FeedLoader.State.Success) {
            initErrorStatus(loader.getState());

            return this;
        }

        mArticles = loader.getRss().getArticles();

        return this;
    }

    private void initErrorStatus(final FeedLoader.State loadStatus) {
        switch (loadStatus) {
            case InternetPermissionDenied:
                setError(ErrorStatus.InternetPermissionDenied);
                break;

            case BadConnection:
                setError(ErrorStatus.BadConnection);
                break;

            case InvalidFormat:
                setError(ErrorStatus.InvalidFormat);
                break;
        }
    }
}
