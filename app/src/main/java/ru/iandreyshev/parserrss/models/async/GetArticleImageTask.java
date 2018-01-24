package ru.iandreyshev.parserrss.models.async;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.app.Utils;
import ru.iandreyshev.parserrss.models.imageProps.IImageProps;
import ru.iandreyshev.parserrss.models.repository.Article;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;

public final class GetArticleImageTask extends Task<Long, Void, Bitmap> {
    private static final int MAX_BYTES_COUNT = 1048576; // 1MB

    private final long mArticleId;
    private final IImageProps mImageProps;

    public static void execute(long articleId, final ITaskListener<Bitmap> listener, final IImageProps props) {
        new GetArticleImageTask(articleId, listener, props)
                .executeOnExecutor(TaskExecutor.getMultiThreadPool());
    }

    @Nullable
    @Override
    protected Bitmap doInBackground(final Long... articles) {
        Bitmap imageBitmap;
        final Article article = App.getDatabase().getArticleById(mArticleId);

        if (article == null) {
            return null;
        } else if (article.getImage() != null) {
            return mImageProps.configure(article.getImage());
        }

        final HttpRequestHandler mRequestHandler = new HttpRequestHandler(article.getImageUrl() == null ? "" : article.getImageUrl());
        mRequestHandler.setMaxContentBytes(MAX_BYTES_COUNT);

        final HttpRequestHandler.State requestResult = mRequestHandler.sendGet();

        final byte[] imageBytes = mRequestHandler.getBody();
        imageBitmap = Utils.toBitmap(imageBytes);

        if (requestResult == HttpRequestHandler.State.Success && imageBitmap != null) {
            App.getDatabase().updateArticleImage(mArticleId, imageBitmap);
            imageBitmap = mImageProps.configure(imageBitmap);
        }

        return imageBitmap;
    }

    private GetArticleImageTask(long articleId, final ITaskListener<Bitmap> listener, final IImageProps props) {
        super(listener);
        mArticleId = articleId;
        mImageProps = props;
    }
}
