package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import ru.iandreyshev.parserrss.app.IEvent;
import ru.iandreyshev.parserrss.models.database.DbFacade;
import ru.iandreyshev.parserrss.models.rss.IViewRss;

public class DeleteRssFromDbTask extends Task<IViewRss, Void, IViewRss> {
    private static final String TAG = DeleteRssFromDbTask.class.getName();

    private final DbFacade mDatabase = new DbFacade();
    private IEventListener mListener;
    private IViewRss mRssToDelete;
    private IEvent mResultEvent;
    private long mNewRssCount;

    private DeleteRssFromDbTask() {
    }

    public static void execute(final IEventListener listener, final IViewRss rssToDelete) {
        final DeleteRssFromDbTask task = new DeleteRssFromDbTask();
        task.setTaskListener(listener);
        task.mListener = listener;
        task.mRssToDelete = rssToDelete;
        task.execute();
    }

    @Override
    protected IViewRss doInBackground(final IViewRss... rssToDelete) {
        try {

            mNewRssCount = mDatabase.getRssCount();
            mDatabase.removeRss(mRssToDelete.getId());
            mNewRssCount = mDatabase.getRssCount();
            mResultEvent = () -> mListener.onSuccess(mNewRssCount, mRssToDelete);

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            mResultEvent = () -> mListener.onFail(mNewRssCount, mRssToDelete);
        }

        return null;
    }

    @Override
    protected void onPostExecute(final IViewRss result) {
        super.onPostExecute(result);
        mResultEvent.doEvent();
    }

    public interface IEventListener extends ITaskListener<IViewRss> {
        void onFail(long count, final IViewRss rss);

        void onSuccess(long count, final IViewRss rss);
    }
}
