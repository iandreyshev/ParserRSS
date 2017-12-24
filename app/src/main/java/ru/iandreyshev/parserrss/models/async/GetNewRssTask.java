package ru.iandreyshev.parserrss.models.async;

import android.os.AsyncTask;
import android.util.Log;

import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

public final class GetNewRssTask extends AsyncTask<String, Void, IViewRss> {
    private static final String TAG = GetNewRssTask.class.getName();
    private static final String REQUEST_NOT_SEND = "The request was not send";
    private static final String BAD_CONNECTION = "Connection error";
    private static final String BAD_URL = "Invalid url";
    private static final String NET_PERMISSION_DENIED = "Internet permission denied";
    private static final String INVALID_RSS_FORMAT = "Invalid rss format";

    private IHttpRequestResult mRequestResult;
    private IFeedView mFeedViewState;
    private String mUrl;

    public static void execute(final IFeedView feedView, final String url) {
        final GetNewRssTask task = new GetNewRssTask();
        task.mFeedViewState = feedView;
        task.mUrl = url;
        task.execute();
    }

    @Override
    protected void onPreExecute() {
        mFeedViewState.startProgressBar(true);
    }

    @Override
    protected IViewRss doInBackground(final String... strings) {
        mRequestResult = new HttpRequestHandler()
                .sendGet(mUrl);

        if (mRequestResult.getState() != HttpRequestHandler.State.Success) {
            return null;
        }

        return Rss.Parser.parse(mRequestResult.getResponseBody(), mUrl);
    }

    @Override
    protected void onPostExecute(final IViewRss rss) {
        mFeedViewState.startProgressBar(false);

        if (mRequestResult.getState() != IHttpRequestResult.State.Success) {
            handleWebError();
        } else if (rss == null) {
            mFeedViewState.showShortToast(INVALID_RSS_FORMAT);
        } else {
            mFeedViewState.insertRss(rss);
        }
    }

    private GetNewRssTask() {
    }

    private void handleWebError() {
        switch (mRequestResult.getState()) {
            case NotSend:
                mFeedViewState.showShortToast(REQUEST_NOT_SEND);
                mFeedViewState.startProgressBar(false);
                Log.e(TAG, REQUEST_NOT_SEND);
                break;

            case BadUrl:
                mFeedViewState.showShortToast(BAD_URL);
                mFeedViewState.startProgressBar(false);
                break;

            case BadConnection:
                mFeedViewState.showShortToast(BAD_CONNECTION);
                mFeedViewState.startProgressBar(false);
                break;

            case PermissionDenied:
                mFeedViewState.showShortToast(NET_PERMISSION_DENIED);
                mFeedViewState.startProgressBar(false);
                break;
        }
    }
}
