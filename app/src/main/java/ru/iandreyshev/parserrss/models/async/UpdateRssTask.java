package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.rss.RssFeed;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;

public class UpdateRssTask extends Task<RssFeed, Void, Rss, UpdateRssTask.ErrorState> {
    private static final int MIN_FEEDS_COUNT = 1;

    private HttpRequestHandler mRequestHandler = new HttpRequestHandler();

    public enum ErrorState {
        InternetPermissionDenied,
        InvalidUrl,
        BadConnection,
        InvalidFormat,
    }

    @Override
    protected Rss behaviourProcess(RssFeed[] feedsCollection) {
        if (feedsCollection.length < MIN_FEEDS_COUNT) {
            Log.e("Update task", "Url not found");
            setError(ErrorState.InvalidUrl);

            return null;
        }

        final RssFeed feed = feedsCollection[0];
        Log.e("Update task", feed.getUrl() == null ? "NULL" : "NOT NULL");
        mRequestHandler.sendGet(feed.getUrl());

        if (mRequestHandler.getState() != HttpRequestHandler.State.Success) {
            initWebError();

            return null;
        }

        final Rss rss = Rss.parse(mRequestHandler.getResponseBody());

        if (rss == null) {
            setError(ErrorState.InvalidFormat);

            return null;
        }

        rss.setUrl(feed.getUrl());

        return rss;
    }

    private void initWebError() {
        switch (mRequestHandler.getState()) {
            case InvalidUrl:
                setError(ErrorState.InvalidUrl);
                break;
            case BadConnection:
                setError(ErrorState.BadConnection);
                break;
            case PermissionDenied:
                setError(ErrorState.InternetPermissionDenied);
                break;
        }
    }
}
