package ru.iandreyshev.parserrss.models.async;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.repository.Article;
import ru.iandreyshev.parserrss.models.repository.Database;
import ru.iandreyshev.parserrss.models.repository.Rss;
import ru.iandreyshev.parserrss.models.rss.ViewArticle;
import ru.iandreyshev.parserrss.models.rss.ViewRss;

public final class GetArticleFromDbTask extends Task<Long, Void, Void> {
    private static final String TAG = GetArticleFromDbTask.class.getName();

    private final IEventListener mListener;
    private final Database mDatabase = App.getDatabase();
    private final long mArticleId;
    private Rss mResultRss;
    private Article mResultArticle;

    public static void execute(long id, final IEventListener listener) {
        new GetArticleFromDbTask(id, listener)
                .executeOnExecutor(TaskExecutor.getMultiThreadPool());
    }

    public interface IEventListener extends ITaskListener<Void> {
        void onSuccess(@NonNull final ViewRss rss, @NonNull final ViewArticle article);

        void onFail();
    }

    @Nullable
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
            mListener.onSuccess(new ViewRss(mResultRss), new ViewArticle(mResultArticle));
        } else {
            mListener.onFail();
        }
    }

    private GetArticleFromDbTask(long id, final IEventListener listener) {
        super(listener);
        mListener = listener;
        mArticleId = id;
    }
}
