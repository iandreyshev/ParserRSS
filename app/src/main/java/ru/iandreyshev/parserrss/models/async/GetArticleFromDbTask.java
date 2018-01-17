package ru.iandreyshev.parserrss.models.async;

import android.support.annotation.NonNull;
import android.util.Log;

import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.repository.Article;
import ru.iandreyshev.parserrss.models.repository.Database;
import ru.iandreyshev.parserrss.models.repository.Rss;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;
import ru.iandreyshev.parserrss.models.rss.IViewRss;

public final class GetArticleFromDbTask extends Task<Long, Void, Void> {
    private static final String TAG = GetArticleFromDbTask.class.getName();

    private IEventListener mListener;
    private final Database mDatabase = App.getDatabase();
    private long mArticleId;
    private Rss mResultRss;
    private Article mResultArticle;

    public static void execute(long id, final IEventListener listener) {
        final GetArticleFromDbTask task = new GetArticleFromDbTask(listener);
        task.mListener = listener;
        task.mArticleId = id;
        task.executeOnExecutor(TaskExecutor.getMultiThreadPool());
    }

    public interface IEventListener extends ITaskListener<Void> {
        void onSuccess(@NonNull final IViewRss rss, @NonNull final IViewArticle article);

        void onFail();
    }

    @Override
    protected Void doInBackground(Long... longs) {
        try {

            mResultArticle = mDatabase.getArticleById(mArticleId);

            if (mResultArticle != null) {
                mResultRss = mDatabase.getRssById(mResultArticle.getRssId());
            }

            return null;

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (mResultArticle != null && mResultRss != null) {
            mListener.onSuccess(mResultRss, mResultArticle);
        } else {
            mListener.onFail();
        }
    }

    private GetArticleFromDbTask(final ITaskListener<Void> listener) {
        super(listener);
    }
}
