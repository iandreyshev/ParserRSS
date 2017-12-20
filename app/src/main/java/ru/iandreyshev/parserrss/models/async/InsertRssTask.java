package ru.iandreyshev.parserrss.models.async;

import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;
import ru.iandreyshev.parserrss.models.web.Url;

public class InsertRssTask extends Task<String, Void, Rss, InsertRssTask.ErrorState> {
    private static final int MIN_URLS_COUNT = 1;

    private HttpRequestHandler mRequestHandler = new HttpRequestHandler();

    public enum ErrorState {
        InvalidUrl,
        BadConnection,
        InternetPermissionDenied,
        InvalidFormat,
    }

    @Override
    protected Rss behaviourProcess(String[] urlCollection) {
        try{
            Thread.sleep(1500);
        } catch (Exception ex) {
        }
        if (urlCollection.length < MIN_URLS_COUNT) {
            setError(ErrorState.InvalidUrl);

            return null;
        }

        final Url feedUrl = Url.parse(urlCollection[0]);
        mRequestHandler.sendGet(feedUrl);

        if (mRequestHandler.getState() != HttpRequestHandler.State.Success) {
            initWebError();

            return null;
        }

        final Rss rss = Rss.parse(mRequestHandler.getResponseBody());

        if (rss == null) {
            setError(ErrorState.InvalidFormat);

            return null;
        }

        rss.setUrl(feedUrl);

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
