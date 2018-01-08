package ru.iandreyshev.parserrss.models.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;

public final class GetImageFromNetTask extends Task<String, Void, Bitmap> {
    private IEventListener mListener;
    private HttpRequestHandler mRequestHandler;

    public static void execute(final String url, final IEventListener listener) {
        final GetImageFromNetTask task = new GetImageFromNetTask(listener);
        task.mListener = listener;
        task.mRequestHandler = new HttpRequestHandler(url);
        task.executeOnExecutor(TaskExecutor.getInstance());
    }

    public interface IEventListener extends ITaskListener<Bitmap> {
        void onSuccess(@NonNull final Bitmap bitmap);
    }

    @Nullable
    @Override
    protected Bitmap doInBackground(final String... strings) {
        final HttpRequestHandler.State result = mRequestHandler.sendGet();

        if (result != HttpRequestHandler.State.Success || mRequestHandler.getResponseBody() == null) {
            return null;
        }

        byte[] data = mRequestHandler.getResponseBody().getBytes();

        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    @Override
    protected void onPostExecute(final Bitmap result) {
        super.onPostExecute(result);

        if (result != null && mListener != null) {
            mListener.onSuccess(result);
        }
    }

    private GetImageFromNetTask(final ITaskListener<Bitmap> listener) {
        super(listener);
    }
}
