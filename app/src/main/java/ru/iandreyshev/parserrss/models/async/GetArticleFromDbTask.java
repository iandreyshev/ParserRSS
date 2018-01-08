package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import ru.iandreyshev.parserrss.models.repository.Database;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;

public final class GetArticleFromDbTask extends Task<Long, Void, IViewArticle> {
    private static final String TAG = GetArticleFromDbTask.class.getName();

    private final Database mDatabase = new Database();
    private long mArticleId;

    public static void execute(long id, final ITaskListener<IViewArticle> listener) {
        final GetArticleFromDbTask task = new GetArticleFromDbTask(listener);
        task.mArticleId = id;
        task.executeOnExecutor(TaskExecutor.getInstance());
    }

    @Override
    protected IViewArticle doInBackground(Long... longs) {
        try {

            return mDatabase.getArticleById(mArticleId);

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            return null;
        }
    }

    private GetArticleFromDbTask(final ITaskListener<IViewArticle> listener) {
        super(listener);
    }
}
