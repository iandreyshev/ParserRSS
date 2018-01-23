package ru.iandreyshev.parserrss.models.async;

import javax.annotation.Nullable;

import ru.iandreyshev.parserrss.models.rss.ViewRss;
import ru.iandreyshev.parserrss.models.rss.RssParser;
import ru.iandreyshev.parserrss.models.repository.Rss;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;

abstract class GetRssFromNetTask extends Task<String, Void, ViewRss> {
    private final HttpRequestHandler mRequestHandler;
    private final IEventListener mListener;
    private Rss mNewRss;

    protected interface IEventListener extends ITaskListener<ViewRss> {
        void onInvalidUrl();

        void onNetError(final IHttpRequestResult requestResult);

        void onParserError();

        void onSuccess(final ViewRss result);
    }

    GetRssFromNetTask(final IEventListener listener, String url) {
        super(listener);
        mListener = listener;
        mRequestHandler = new HttpRequestHandler(url);
    }

    @Nullable
    protected abstract Rss onSuccess(final Rss rss);

    boolean isUrlValid() {
        if (mRequestHandler.getState() != HttpRequestHandler.State.BadUrl) {
            return true;
        }

        setResultEvent(mListener::onInvalidUrl);

        return false;
    }

    boolean getRssFromNet() {
        mRequestHandler.sendGet();

        if (mRequestHandler.getState() == HttpRequestHandler.State.Success) {
            return true;
        }

        setResultEvent(() -> mListener.onNetError(mRequestHandler));

        return false;
    }

    boolean parseRss() {
        if ((mNewRss = RssParser.parse(mRequestHandler.getResponseBodyAsString())) != null) {
            mNewRss.setUrl(mRequestHandler.getUrlStr());

            return true;
        }

        setResultEvent(mListener::onParserError);

        return false;
    }

    @Nullable
    @Override
    protected final ViewRss doInBackground(final String... strings) {
        if (!isUrlValid() || !getRssFromNet() || !parseRss()) {
            return null;
        }

        final Rss rss = onSuccess(mNewRss);

        return rss == null ? null : new ViewRss(rss);
    }

    @Nullable
    protected final String getUrl() {
        return mRequestHandler.getUrlStr();
    }
}
