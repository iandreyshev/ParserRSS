package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import ru.iandreyshev.parserrss.app.IEvent;
import ru.iandreyshev.parserrss.models.database.DbFacade;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;

public final class GetRssFromNetTask extends Task<String, Void, IViewRss> {
    private static final String TAG = GetRssFromNetTask.class.getName();

    private final DbFacade mDatabase = new DbFacade();
    private IHttpRequestResult mNetRequestResult;
    private IEventListener mListener;
    private IEvent mResultEvent;
    private String mUrl;
    private Rss mRss;

    private GetRssFromNetTask() {
    }

    public static void execute(final IEventListener listener, final String url) {
        final GetRssFromNetTask task = new GetRssFromNetTask();
        task.setTaskListener(listener);
        task.mListener = listener;
        task.mUrl = url;
        task.execute();
    }

    @Override
    protected IViewRss doInBackground(final String... strings) {
        if (isRssExist()) {
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

    private boolean isRssExist() {
        try {
            if (mDatabase.getRssCount(mUrl) > 0) {
                mResultEvent = () -> mListener.onDuplicateRss();

                return true;
            }
        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            mResultEvent = () -> mListener.onDbError();

            return true;
        }

        return false;
    }

    private boolean getRssFromNet() {
        mNetRequestResult = new HttpRequestHandler().sendGet(mUrl);

        if (mNetRequestResult.getState() != HttpRequestHandler.State.Success) {
            mResultEvent = () -> mListener.onNetError(mNetRequestResult);

            return false;
        }

        return true;
    }

    private boolean parseRss() {
        mRss = Rss.Parser.parse(mNetRequestResult.getResponseBody());

        if (mRss == null) {
            return false;
        }

        mRss.setUrl(mUrl);

        return true;
    }

    private boolean saveRssToDatabase() {
        try {
            if (!mDatabase.putRssIfNotExist(mRss)) {
                mResultEvent = () -> mListener.onDuplicateRss();

                return false;
            }

            mDatabase.putArticles(mRss, mRss.getRssArticles());

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            mResultEvent = () -> mListener.onDbError();

            return false;
        }

        return true;
    }

    public interface IEventListener extends ITaskListener<IViewRss> {
        void onNetError(final IHttpRequestResult requestResult);

        void onParsingError();

        void onDbError();

        void onDuplicateRss();

        void onSuccess(final IViewRss rss);
    }
}
