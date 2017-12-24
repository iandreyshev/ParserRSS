package ru.iandreyshev.parserrss.models.async;

import android.os.AsyncTask;
import android.util.Log;

import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

public class UpdateRssTask extends AsyncTask<IViewRss, Void, IViewRss> {
    private static final String TAG = UpdateRssTask.class.getName();
    private static final String REQUEST_NOT_SEND = "The request was not send";
    private static final String BAD_CONNECTION = "Connection error";
    private static final String BAD_URL = "Invalid url";
    private static final String NET_PERMISSION_DENIED = "Internet permission denied";
    private static final String INVALID_RSS_FORMAT = "Invalid rss format";

    private IHttpRequestResult mRequestResult;
    private IFeedView mFeedViewState;
    private IViewRss mRss;

    public static void execute(final IFeedView feedView, final IViewRss rssToUpdate) {
        final UpdateRssTask task = new UpdateRssTask();
        task.mRss = rssToUpdate;
        task.mFeedViewState = feedView;
        task.execute();
    }

    @Override
    protected void onPreExecute() {
        mFeedViewState.startUpdate(mRss, true);
    }

    @Override
    protected IViewRss doInBackground(final IViewRss... feedsToUpdate) {
        mRequestResult = new HttpRequestHandler()
                .sendGet(mRss.getUrl());

        if (mRequestResult.getState() != IHttpRequestResult.State.Success) {
            return null;
        }

        return Rss.Parser.parse(mRequestResult.getResponseBody(), mRss.getUrl());
    }

    @Override
    public void onPostExecute(final IViewRss rss) {
        Log.e(TAG, "End update");
        Log.e(TAG, "Handler state " + mRequestResult.getState());
        mFeedViewState.startUpdate(mRss, false);

        if (mRequestResult.getState() != IHttpRequestResult.State.Success) {
            handleWebError();
        } else if (rss == null) {
            mFeedViewState.showShortToast(INVALID_RSS_FORMAT);
        } else {
            mFeedViewState.updateArticles(rss);
        }
    }

    private UpdateRssTask() {
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
