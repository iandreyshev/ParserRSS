package ru.iandreyshev.parserrss.models.async;

import android.os.AsyncTask;
import android.util.Log;

import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;
import ru.iandreyshev.parserrss.presentation.presenter.IFeedViewHost;

public class UpdateRssTask extends AsyncTask<IViewRss, Void, IViewRss> {
    private static final String TAG = UpdateRssTask.class.getName();
    private static final String REQUEST_NOT_SEND = "The request was not send";
    private static final String BAD_CONNECTION = "Connection error";
    private static final String BAD_URL = "Invalid url";
    private static final String NET_PERMISSION_DENIED = "Internet permission denied";
    private static final String INVALID_RSS_FORMAT = "Invalid rss format";

    private IHttpRequestResult mRequestResult;
    private IFeedViewHost mViewHost;
    private IViewRss mRss;

    private UpdateRssTask() {
    }

    public static void execute(final IFeedViewHost viewHost, final IViewRss rssToUpdate) {
        final UpdateRssTask task = new UpdateRssTask();
        task.mRss = rssToUpdate;
        task.mViewHost = viewHost;
        task.execute();
    }

    @Override
    protected void onPreExecute() {
        mViewHost.getViewState().startUpdate(mRss, true);
    }

    @Override
    protected IViewRss doInBackground(final IViewRss... feedsToUpdate) {
        mRequestResult = new HttpRequestHandler()
                .sendGet(mRss.getUrl());

        if (mRequestResult.getState() != IHttpRequestResult.State.Success) {
            return null;
        }

        return Rss.Parser.parse(mRequestResult.getResponseBody());
    }

    @Override
    public void onPostExecute(final IViewRss rss) {
        mViewHost.getViewState().startUpdate(mRss, false);

        if (mRequestResult.getState() != IHttpRequestResult.State.Success) {
            handleWebError();
        } else if (rss == null) {
            mViewHost.getViewState().showShortToast(INVALID_RSS_FORMAT);
        } else {
            mViewHost.getViewState().updateArticles(rss);
        }
    }

    private void handleWebError() {
        switch (mRequestResult.getState()) {
            case NotSend:
                mViewHost.getViewState().showShortToast(REQUEST_NOT_SEND);
                mViewHost.getViewState().startProgressBar(false);
                Log.e(TAG, REQUEST_NOT_SEND);
                break;

            case BadUrl:
                mViewHost.getViewState().showShortToast(BAD_URL);
                mViewHost.getViewState().startProgressBar(false);
                break;

            case BadConnection:
                mViewHost.getViewState().showShortToast(BAD_CONNECTION);
                mViewHost.getViewState().startProgressBar(false);
                break;

            case PermissionDenied:
                mViewHost.getViewState().showShortToast(NET_PERMISSION_DENIED);
                mViewHost.getViewState().startProgressBar(false);
                break;
        }
    }
}
