package ru.iandreyshev.parserrss.models.async;

import javax.annotation.Nullable;

import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.RssParser;
import ru.iandreyshev.parserrss.models.repository.Rss;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;

abstract class GetRssFromNetTask extends Task<String, Void, IViewRss> {
    private HttpRequestHandler mRequestHandler;
    private Rss mNewRss;
    private IEventListener mListener;

    protected interface IEventListener extends ITaskListener<IViewRss> {
        void onInvalidUrl();

        void onNetError(final IHttpRequestResult requestResult);

        void onParserError();

        void onSuccess(final IViewRss result);
    }

    GetRssFromNetTask(final IEventListener listener, final String url) {
        super(listener);
        mRequestHandler = new HttpRequestHandler(url);
        mListener = listener;
    }

    protected void onInvalidUrl() {
        // Implement in subclasses if it need
    }

    protected void onNetError(final IHttpRequestResult requestResult) {
        // Implement in subclasses if it need
    }

    protected void onParserError() {
        // Implement in subclasses if it need
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
        if ((mNewRss = RssParser.parse(mRequestHandler.getResponseBodyAsString())) != null) {
            mNewRss.setUrl(mRequestHandler.getUrlStr());

            return true;
        }

        setResultEvent(() -> mListener.onParserError());

        return false;
    }

    @Nullable
    @Override
    protected final IViewRss doInBackground(final String... strings) {
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
