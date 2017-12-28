package ru.iandreyshev.parserrss.models.async;

import javax.annotation.Nullable;

import ru.iandreyshev.parserrss.models.rss.RssParser;
import ru.iandreyshev.parserrss.models.rss.ViewRss;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;

abstract class GetRssFromNetTask extends Task<String, Void, ViewRss> {
    private static final String TAG = GetRssFromNetTask.class.getName();

    private HttpRequestHandler mRequestHandler;
    private Rss mNewRss;
    private IEventListener mListener;

    protected interface IEventListener extends ITaskListener<ViewRss> {
        void onInvalidUrl();

        void onNetError(final IHttpRequestResult requestResult);

        void onParserError();

        void onSuccess(final ViewRss result);
    }

    GetRssFromNetTask(final IEventListener listener, final String url) {
        setTaskListener(listener);
        mRequestHandler = new HttpRequestHandler(url);
        mListener = listener;
    }

    protected void onInvalidUrl() {
    }

    protected void onNetError(final IHttpRequestResult requestResult) {
    }

    protected void onParserError() {
    }

    protected abstract Rss onSuccess(final Rss rss);

    protected boolean isUrlValid() {
        if (mRequestHandler.getState() != HttpRequestHandler.State.BadUrl) {
            return true;
        }

        setResultEvent(() -> mListener.onInvalidUrl());

        return false;
    }

    protected boolean getRssFromNet() {
        mRequestHandler.sendGet();

        if (mRequestHandler.getState() == HttpRequestHandler.State.Success) {
            return true;
        }

        setResultEvent(() -> mListener.onNetError(mRequestHandler));

        return false;
    }

    protected boolean parseRss() {
        if ((mNewRss = RssParser.parse(mRequestHandler.getResponseBody())) != null) {
            mNewRss.setUrl(mRequestHandler.getUrlStr());
            setResultEvent(() -> mListener.onParserError());

            return true;
        }

        return false;
    }

    @Nullable
    @Override
    protected final ViewRss doInBackground(final String... strings) {
        if (!isUrlValid()) {
            onInvalidUrl();

            return null;

        } else if (!getRssFromNet()) {
            onNetError(mRequestHandler);

            return null;

        } else if (!parseRss()) {
            onParserError();

            return null;

        }

        return onSuccess(mNewRss);
    }

    @Nullable
    protected final String getUrl() {
        return mRequestHandler.getUrlStr();
    }
}
