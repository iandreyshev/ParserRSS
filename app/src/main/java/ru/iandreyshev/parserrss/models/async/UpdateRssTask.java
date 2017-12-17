package ru.iandreyshev.parserrss.models.async;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.models.rss.IRssArticle;
import ru.iandreyshev.parserrss.models.rss.IRssFeed;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;

public class UpdateRssTask
        extends Task<IRssFeed, Void, Rss, UpdateRssTask.ErrorState> {
    private HttpRequestHandler mRequestHandler = new HttpRequestHandler();

    public enum ErrorState {
        InternetPermissionDenied,
        InvalidUrl,
        BadConnection,
        InvalidFormat,
    }

    @Override
    protected Rss doInBackground(IRssFeed... feedsCollection) {
        IRssFeed feed = feedsCollection[0];
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
