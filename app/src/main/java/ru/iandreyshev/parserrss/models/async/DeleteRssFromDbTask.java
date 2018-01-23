package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import javax.annotation.Nullable;

import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.repository.Database;
import ru.iandreyshev.parserrss.models.rss.ViewRss;

public final class DeleteRssFromDbTask extends Task<ViewRss, Void, ViewRss> {
    private static final String TAG = DeleteRssFromDbTask.class.getName();

    private final Database mDatabase = App.getDatabase();
    private final IEventListener mListener;
    private final ViewRss mRssToDelete;

    public static void execute(final IEventListener listener, final ViewRss rssToDelete) {
        new DeleteRssFromDbTask(listener, rssToDelete).
                executeOnExecutor(TaskExecutor.getMultiThreadPool());
    }

    public interface IEventListener extends ITaskListener<ViewRss> {
        void onFail(final ViewRss rss);
    }

    @Nullable
    @Override
    protected ViewRss doInBackground(final ViewRss... rssToDelete) {
        try {

            mDatabase.removeRssById(mRssToDelete.getId());

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            setResultEvent(() -> mListener.onFail(mRssToDelete));
        }

        return mRssToDelete;
    }

    private DeleteRssFromDbTask(final IEventListener listener, final ViewRss rssToDelete) {
        super(listener);
        mListener = listener;
        mRssToDelete = rssToDelete;
    }
}
