package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import ru.iandreyshev.parserrss.app.IEvent;
import ru.iandreyshev.parserrss.models.database.RssDatabase;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;

public final class GetRssFromNetTask extends Task<String, Void, IViewRss> {
    private static final String TAG = GetRssFromNetTask.class.getName();

    private final RssDatabase mDatabase = new RssDatabase();
    private HttpRequestHandler mRequestHandler;
    private IEventListener mListener;
    private IEvent mResultEvent;
    private Rss mRss;

    private GetRssFromNetTask() {
    }

    public static void execute(final IEventListener listener, final String url) {
        final GetRssFromNetTask task = new GetRssFromNetTask();
        task.setTaskListener(listener);
        task.mRequestHandler = new HttpRequestHandler(url);
        task.mListener = listener;
        task.execute();
    }

    @Override
    protected IViewRss doInBackground(final String... strings) {
        if (!validateUrl()) {
            return null;
        } else if (!validateExistence()) {
            return null;
        } else if (!getRssFromNet()) {
            return null;
        } else if (!parseRss()) {
            return null;
        } else if (!saveRssToDatabase()) {
            return null;
        }

        mResultEvent = () -> mListener.onSuccess(mRss);

        return mRss;
    }

    @Override
    protected void onPostExecute(final IViewRss rss) {
        super.onPostExecute(rss);
        mResultEvent.doEvent();
    }

    private boolean validateUrl() {
        if (mRequestHandler.getState() == HttpRequestHandler.State.BadUrl) {
            mResultEvent = () -> mListener.onInvalidUrl();

            return false;
        }

        return true;
    }

    private boolean validateExistence() {
        try {

            if (mDatabase.getRssCountByUrl(mRequestHandler.getUrlStr()) == 0) {
                return true;
            }

            mResultEvent = () -> mListener.onDuplicateRss();

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            mResultEvent = () -> mListener.onDbError();
        }

        return true;
    }

    private boolean getRssFromNet() {
        mRequestHandler.sendGet();

        if (mRequestHandler.getState() != HttpRequestHandler.State.Success) {
            mResultEvent = () -> mListener.onNetError(mRequestHandler);

            return false;
        }

        return true;
    }

    private boolean parseRss() {
        if ((mRss = Rss.Parser.parse(mRequestHandler.getResponseBody())) == null) {
            mResultEvent = () -> mListener.onParsingError();

            return false;
        }

        mRss.setUrl(mRequestHandler.getUrlStr());

        return true;
    }

    private boolean saveRssToDatabase() {
        try {

            if (mDatabase.putRssIfSameUrlNotExist(mRss)) {
                return true;
            }

            mResultEvent = () -> mListener.onDuplicateRss();

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            mResultEvent = () -> mListener.onDbError();
        }

        return false;
    }

    public interface IEventListener extends ITaskListener<IViewRss> {
        void onInvalidUrl();

        void onNetError(final IHttpRequestResult requestResult);

        void onParsingError();

        void onDbError();

        void onDuplicateRss();

        void onSuccess(final IViewRss rss);
    }
}
