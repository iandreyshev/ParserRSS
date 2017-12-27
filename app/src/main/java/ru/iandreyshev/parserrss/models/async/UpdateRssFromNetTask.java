package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import ru.iandreyshev.parserrss.app.IEvent;
import ru.iandreyshev.parserrss.models.database.RssDatabase;
import ru.iandreyshev.parserrss.models.rss.RssParser;
import ru.iandreyshev.parserrss.models.rss.ViewRss;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;

public final class UpdateRssFromNetTask extends Task<ViewRss, Void, ViewRss> {
    private static final String TAG = UpdateRssFromNetTask.class.getName();

    private final RssDatabase mDatabase = new RssDatabase();
    private HttpRequestHandler mRequestHandler;
    private IEventListener mListener;
    private IEvent mResultEvent;
    private ViewRss mUpdatingRss;
    private Rss mRssFromNet;

    private UpdateRssFromNetTask() {
    }

    public static void execute(final IEventListener listener, final ViewRss rssToUpdate) {
        final UpdateRssFromNetTask task = new UpdateRssFromNetTask();
        task.setTaskListener(listener);
        task.mRequestHandler = new HttpRequestHandler(rssToUpdate.getUrl());
        task.mUpdatingRss = rssToUpdate;
        task.mListener = listener;
        task.execute();
    }

    @Override
    protected ViewRss doInBackground(final ViewRss... feedsToUpdate) {
        if (!validateRssExistence()) {
            return null;
        } else if (!getRssFromNet()) {
            return null;
        } else if (!parseRss()) {
            return null;
        } else if (!updateRssInDatabase()) {
            return null;
        }

        mResultEvent = () -> mListener.onSuccess(mUpdatingRss);

        return mUpdatingRss;
    }

    @Override
    public void onPostExecute(final ViewRss rss) {
        super.onPostExecute(rss);
        mResultEvent.doEvent();
    }

    private boolean validateRssExistence() {
        try {

            if (mDatabase.getRssCountByUrl(mRequestHandler.getUrlStr()) > 0) {
                return true;
            }

            mResultEvent = () -> mListener.onRssNotExist(mUpdatingRss);

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            mResultEvent = () -> mListener.onDbError(mUpdatingRss);
        }

        return true;
    }

    private boolean getRssFromNet() {
        mRequestHandler.sendGet();

        if (mRequestHandler.getState() != HttpRequestHandler.State.Success) {
            mResultEvent = () -> mListener.onNetError(mUpdatingRss, mRequestHandler);

            return false;
        }

        return true;
    }

    private boolean parseRss() {
        if ((mRssFromNet = RssParser.parse(mRequestHandler.getResponseBody())) == null) {
            mResultEvent = () -> mListener.onParsingError(mUpdatingRss);

            return false;
        }

        mRssFromNet.setUrl(mRequestHandler.getUrlStr());

        return false;
    }

    private boolean updateRssInDatabase() {
        try {

            if (mDatabase.updateRssIfExistByUrl(mRssFromNet)) {
                return true;
            }

            mResultEvent = () -> mListener.onRssNotExist(mUpdatingRss);

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            mResultEvent = () -> mListener.onDbError(mUpdatingRss);
        }

        return false;
    }

    public interface IEventListener extends ITaskListener<ViewRss> {
        void onRssNotExist(final ViewRss rss);

        void onNetError(final ViewRss rss, final IHttpRequestResult requestResult);

        void onParsingError(final ViewRss rss);

        void onDbError(final ViewRss rss);

        void onSuccess(final ViewRss rss);
    }
}
