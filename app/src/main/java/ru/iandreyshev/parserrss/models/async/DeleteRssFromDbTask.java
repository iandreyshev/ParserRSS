package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import javax.annotation.Nullable;

import ru.iandreyshev.parserrss.app.IEvent;
import ru.iandreyshev.parserrss.models.repository.Database;
import ru.iandreyshev.parserrss.models.rss.IViewRss;

public final class DeleteRssFromDbTask extends Task<IViewRss, Void, IViewRss> {
    private static final String TAG = DeleteRssFromDbTask.class.getName();

    private final Database mDatabase = new Database();
    private IEventListener mListener;
    private IViewRss mRssToDelete;
    private IEvent mResultEvent;

    public static void execute(final IEventListener listener, final IViewRss rssToDelete) {
        if (rssToDelete == null) {
            return;
        }

        final DeleteRssFromDbTask task = new DeleteRssFromDbTask();
        task.setTaskListener(listener);
        task.mListener = listener;
        task.mRssToDelete = rssToDelete;
        task.execute();
    }

    public interface IEventListener extends ITaskListener<IViewRss> {
        void onFail(final IViewRss rss);

        void onSuccess(final IViewRss rss);
    }

    @Nullable
    @Override
    protected IViewRss doInBackground(final IViewRss... rssToDelete) {
        try {

            mDatabase.removeRss(mRssToDelete.getId());
            mResultEvent = () -> mListener.onSuccess(mRssToDelete);

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            mResultEvent = () -> mListener.onFail(mRssToDelete);
        }

        return mRssToDelete;
    }

    @Override
    protected void onPostExecute(final IViewRss result) {
        super.onPostExecute(mRssToDelete);
        mResultEvent.doEvent();
    }

    private DeleteRssFromDbTask() {
    }
}
