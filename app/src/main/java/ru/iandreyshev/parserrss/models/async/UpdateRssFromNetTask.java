package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import java.util.ArrayList;

import ru.iandreyshev.parserrss.app.IEvent;
import ru.iandreyshev.parserrss.models.database.DbFacade;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.rss.RssArticle;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;

public class UpdateRssFromNetTask extends Task<IViewRss, Void, IViewRss> {
    private static final String TAG = UpdateRssFromNetTask.class.getName();

    private final ArrayList<RssArticle> mNewArticles = new ArrayList<>();
    private final DbFacade mDatabase = new DbFacade();
    private IHttpRequestResult mNetRequestResult;
    private IEventListener mListener;
    private IEvent mResultEvent;
    private IViewRss mUpdatingRss;
    private String mUrl;

    private UpdateRssFromNetTask() {
    }

    public static void execute(final IEventListener listener, final IViewRss rssToUpdate) {
        final UpdateRssFromNetTask task = new UpdateRssFromNetTask();
        task.setTaskListener(listener);
        task.mUpdatingRss = rssToUpdate;
        task.mUrl = rssToUpdate.getUrl();
        task.mListener = listener;
        task.execute();
    }

    @Override
    protected IViewRss doInBackground(final IViewRss... feedsToUpdate) {
        if (!isRssExist()) {
            return null;
        } else if (!getRssFromNet()) {
            return null;
        } else if (!parseRss()) {
            return null;
        } else if (!updateArticlesInRss()) {
            return null;
        }

        mNetRequestResult = new HttpRequestHandler()
                .sendGet(mUpdatingRss.getUrl());

        if (mNetRequestResult.getState() != IHttpRequestResult.State.Success) {
            return null;
        }

        return Rss.Parser.parse(mNetRequestResult.getResponseBody());
    }

    @Override
    public void onPostExecute(final IViewRss rss) {
        super.onPostExecute(rss);
        //mResultEvent.doEvent();
    }

    private boolean isRssExist() {
        try {
            if (mDatabase.getRssCount(mUrl) == 0) {
                mResultEvent = () -> mListener.onRssDeleted(mUpdatingRss);

                return false;
            }
        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            mResultEvent = () -> mListener.onDbError(mUpdatingRss);

            return false;
        }

        return true;
    }

    private boolean getRssFromNet() {
        mNetRequestResult = new HttpRequestHandler().sendGet(mUrl);

        if (mNetRequestResult.getState() != HttpRequestHandler.State.Success) {
            mResultEvent = () -> mListener.onNetError(mUpdatingRss, mNetRequestResult);

            return false;
        }

        return true;
    }

    private boolean parseRss() {
        // TODO: Parsing rss

        return false;
    }

    private boolean updateArticlesInRss() {
        // TODO: Update articles in database

        return false;
    }

    public interface IEventListener extends ITaskListener<IViewRss> {
        void onNetError(final IViewRss rss, final IHttpRequestResult requestResult);

        void onParsingError(final IViewRss rss);

        void onRssDeleted(final IViewRss rss);

        void onDbError(final IViewRss rss);

        void onSuccess(final IViewRss rss);
    }

}
