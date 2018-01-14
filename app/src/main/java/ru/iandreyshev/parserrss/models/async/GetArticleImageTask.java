package ru.iandreyshev.parserrss.models.async;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;

import ru.iandreyshev.parserrss.app.Utils;
import ru.iandreyshev.parserrss.models.repository.Article;
import ru.iandreyshev.parserrss.models.repository.Database;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;

public final class GetArticleImageTask extends Task<Long, Void, Bitmap> {
    private static final String TAG = GetArticleImageTask.class.getName();

    private final Database mDatabase = new Database();
    private Long mArticleId;

    public static void execute(Long articleId, final ITaskListener<Bitmap> listener) {
        final GetArticleImageTask task = new GetArticleImageTask(listener);
        task.mArticleId = articleId;
        task.executeOnExecutor(TaskExecutor.getMultiThreadPool());
    }

    @Nullable
    @Override
    protected Bitmap doInBackground(final Long... articles) {
        try {
            final Article article = mDatabase.getArticleById(mArticleId);

            if (article == null) {
                return null;
            } else if (article.getImage() != null) {
                return Utils.toBitmap(article.getImage());
            }

            final HttpRequestHandler mRequestHandler = new HttpRequestHandler(article.getImageUrl());
            final HttpRequestHandler.State result = mRequestHandler.sendGet();
            byte[] imageBytes = mRequestHandler.getResponseBody();

            if (result != HttpRequestHandler.State.Success || imageBytes == null) {
                return null;
            }

            mDatabase.updateArticleImage(mArticleId, imageBytes);

            return Utils.toBitmap(imageBytes);

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));

            return null;
        }
    }

    private GetArticleImageTask(final ITaskListener<Bitmap> listener) {
        super(listener);
    }
}
