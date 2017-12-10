package ru.iandreyshev.parserrss.models.async;

import android.os.AsyncTask;
import android.util.Log;

import ru.iandreyshev.parserrss.models.database.DbFacade;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;
import ru.iandreyshev.parserrss.presentation.presenter.IFeedViewHost;

public final class GetRssFromNetTask extends AsyncTask<String, Void, IViewRss> {
    private static final String TAG = GetRssFromNetTask.class.getName();
    private static final String REQUEST_NOT_SEND = "The request was not send";
    private static final String BAD_CONNECTION = "Connection error";
    private static final String BAD_URL = "Invalid url";
    private static final String NET_PERMISSION_DENIED = "Internet permission denied";
    private static final String INVALID_RSS_FORMAT = "Invalid rss format";
    private static final String DATABASE_ERROR = "Saving error";
    private static final String DUPLICATE_ERROR = "Rss already exist";

    private IHttpRequestResult mRequestResult;
    private IFeedViewHost mViewHost;
    private String mUrl;
    private Rss mRss;
    private IResultEvent mResultEvent;
    private final DbFacade mDbFacade = new DbFacade();

    private GetRssFromNetTask() {
    }

    public static void execute(final IFeedViewHost viewHost, final String url) {
        final GetRssFromNetTask task = new GetRssFromNetTask();
        task.mViewHost = viewHost;
        task.mUrl = url;
        task.execute();
    }

    @Override
    protected void onPreExecute() {
        mViewHost.getViewState().startProgressBar(true);
    }

    @Override
    protected IViewRss doInBackground(final String... strings) {
        if (isRssExist()) {
            return null;
        } else if (!getRssFromWeb()) {
            return null;
        } else if (!parseRss()) {
            return null;
        } else if (!saveRssToDatabase()) {
            return null;
        }

        mResultEvent = () -> mViewHost.getViewState().insertRss(mRss);

        return mRss;
    }

    @Override
    protected void onPostExecute(final IViewRss rss) {
        mViewHost.getViewState().startProgressBar(false);

        if (mResultEvent != null) {
            mResultEvent.doEvent();
        }
    }

    private boolean isRssExist() {
        if (mDbFacade.isRssExist(mUrl)) {
            mResultEvent = this::handleDuplicationError;

            return true;
        }

        return false;
    }

    private boolean getRssFromWeb() {
        mRequestResult = new HttpRequestHandler().sendGet(mUrl);

        if (mRequestResult.getState() != HttpRequestHandler.State.Success) {
            mResultEvent = this::handleWebError;

            return false;
        }

        return true;
    }

    private boolean parseRss() {
        mRss = Rss.Parser.parse(mRequestResult.getResponseBody());

        if (mRss == null) {
            mResultEvent = this::handleParseError;

            return false;
        }
        mRss.setUrl(mUrl);

        return true;
    }

    private boolean saveRssToDatabase() {
        try {
            if (!mDbFacade.putRssIfNotExist(mRss)) {
                mResultEvent = this::handleDuplicationError;

                return false;
            }

            mDbFacade.saveArticles(mRss, mRss.getRssArticles());

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            mResultEvent = this::handleSavingError;

            return false;
        }

        return true;
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

    private void handleParseError() {
        mViewHost.getViewState().showShortToast(INVALID_RSS_FORMAT);
    }

    private void handleSavingError() {
        mViewHost.getViewState().showShortToast(DATABASE_ERROR);
    }

    private void handleDuplicationError() {
        mViewHost.getViewState().showShortToast(DUPLICATE_ERROR);
        mViewHost.getViewState().openAddingRssDialog();
    }

    private interface IResultEvent {
        void doEvent();
    }
}
