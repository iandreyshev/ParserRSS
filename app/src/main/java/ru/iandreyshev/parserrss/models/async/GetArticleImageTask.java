package ru.iandreyshev.parserrss.models.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.app.Utils;
import ru.iandreyshev.parserrss.models.repository.Article;
import ru.iandreyshev.parserrss.models.repository.Database;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;

public final class GetArticleImageTask extends Task<Long, Void, Bitmap> {
    private final Database mDatabase = App.getDatabase();
    private byte[] mResultBytes;
    private Bitmap mResultBitmap;
    private long mArticleId;
    private IEventListener mListener;

    public static void execute(long article, final IEventListener listener) {
        final GetArticleImageTask task = new GetArticleImageTask(listener);
        task.mListener = listener;
        task.mArticleId = article;
        task.executeOnExecutor(TaskExecutor.getMultiThreadPool());
    }

    public interface IEventListener extends ITaskListener<Bitmap> {
        void onSuccess(@NonNull byte[] imageBytes, @NonNull Bitmap bitmap);
    }

    @Nullable
    @Override
    protected Bitmap doInBackground(final Long... articles) {
        final Article article = mDatabase.getArticleById(mArticleId);

        if (article == null) {
            return null;
        } else if (article.getImage() != null) {
            mResultBytes = article.getImage();
            mResultBitmap = Utils.toBitmap(mResultBytes);

            return mResultBitmap;
        }

        final HttpRequestHandler mRequestHandler = new HttpRequestHandler(article.getImageUrl());
        final HttpRequestHandler.State result = mRequestHandler.sendGet();

        mResultBytes = mRequestHandler.getResponseBody();
        mResultBitmap = Utils.toBitmap(mResultBytes);

        if (result == HttpRequestHandler.State.Success && mResultBytes != null && mResultBitmap != null) {
            mDatabase.updateArticleImage(mArticleId, mResultBytes);
        }

        return mResultBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        if (mResultBytes != null && mResultBitmap != null) {
            mListener.onSuccess(mResultBytes, mResultBitmap);
        }
    }

    private GetArticleImageTask(final IEventListener listener) {
        super(listener);
    }
}
