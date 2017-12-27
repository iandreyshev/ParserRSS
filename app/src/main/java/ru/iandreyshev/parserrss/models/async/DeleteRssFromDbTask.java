package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import ru.iandreyshev.parserrss.app.IEvent;
import ru.iandreyshev.parserrss.models.database.RssDatabase;
import ru.iandreyshev.parserrss.models.rss.ViewRss;

public final class DeleteRssFromDbTask extends Task<ViewRss, Void, ViewRss> {
    private static final String TAG = DeleteRssFromDbTask.class.getName();

    private final RssDatabase mDatabase = new RssDatabase();
    private IEventListener mListener;
    private ViewRss mRssToDelete;
    private IEvent mResultEvent;

    private DeleteRssFromDbTask() {
    }

    public static void execute(final IEventListener listener, final ViewRss rssToDelete) {
        final DeleteRssFromDbTask task = new DeleteRssFromDbTask();
        task.setTaskListener(listener);
        task.mListener = listener;
        task.mRssToDelete = rssToDelete;
        task.execute();
    }

    @Override
    protected ViewRss doInBackground(final ViewRss... rssToDelete) {
        try {

            mDatabase.removeRss(mRssToDelete.getId());
            mResultEvent = () -> mListener.onSuccess(mRssToDelete);

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            mResultEvent = () -> mListener.onFail(mRssToDelete);
        }

        return null;
    }

    @Override
    protected void onPostExecute(final ViewRss result) {
        super.onPostExecute(result);
        mResultEvent.doEvent();
    }

    public interface IEventListener extends ITaskListener<ViewRss> {
        void onFail(final ViewRss rss);

        void onSuccess(final ViewRss rss);
    }
}
