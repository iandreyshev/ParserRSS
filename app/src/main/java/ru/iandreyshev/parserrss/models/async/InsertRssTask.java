package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.models.rss.IRssArticle;
import ru.iandreyshev.parserrss.models.rss.IRssFeed;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;
import ru.iandreyshev.parserrss.models.web.Url;

public class InsertRssTask
        extends Task<String, Void, Rss, InsertRssTask.ErrorState> {
    private HttpRequestHandler mRequestHandler = new HttpRequestHandler();

    public enum ErrorState {
        InvalidUrl,
        BadConnection,
        InternetPermissionDenied,
        InvalidFormat,
    }

    @Override
    protected Rss doInBackground(String... urlCollection) {
        mRequestHandler.sendGet(Url.parse(urlCollection[0]));

        if (mRequestHandler.getState() != HttpRequestHandler.State.Success) {
            initWebError();

            return null;
        }

        final String rssText = new String(mRequestHandler.getResponseBody());
        final Rss rss = Rss.parse(rssText);

        if (rss == null) {
            setError(ErrorState.InvalidFormat);

            return null;
        }

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
