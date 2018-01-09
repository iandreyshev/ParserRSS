package ru.iandreyshev.parserrss.models.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import ru.iandreyshev.parserrss.models.repository.Database;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;

public final class GetImageFromNetTask extends Task<IViewArticle, Void, Bitmap> {
    private static final String TAG = GetImageFromNetTask.class.getName();

    private final Database mDatabase = new Database();
    private HttpRequestHandler mRequestHandler;
    private IViewArticle mArticle;

    public static void execute(final IViewArticle article, final ITaskListener<Bitmap> listener) {
        final GetImageFromNetTask task = new GetImageFromNetTask(listener);
        task.mArticle = article;
        task.mRequestHandler = new HttpRequestHandler(article.getImageUrl());
        task.executeOnExecutor(TaskExecutor.getMultiThreadPool());
    }

    @Nullable
    @Override
    protected Bitmap doInBackground(final IViewArticle... articles) {
        final HttpRequestHandler.State result = mRequestHandler.sendGet();
        byte[] image = mRequestHandler.getResponseBody();

        if (result != HttpRequestHandler.State.Success || image == null) {
            Log.e(getClass().getName(), result.toString());
            return null;
        }

        try {
            mDatabase.updateArticleImage(mArticle.getId(), image);
            mArticle.setImage(image);
        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));

            return null;
        }

        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    private GetImageFromNetTask(final ITaskListener<Bitmap> listener) {
        super(listener);
    }
}
