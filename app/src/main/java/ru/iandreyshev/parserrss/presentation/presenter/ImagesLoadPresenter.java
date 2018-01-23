package ru.iandreyshev.parserrss.presentation.presenter;

import android.graphics.Bitmap;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.lang.ref.WeakReference;
import java.util.HashSet;

import ru.iandreyshev.parserrss.models.async.GetArticleImageTask;
import ru.iandreyshev.parserrss.models.async.ITaskListener;
import ru.iandreyshev.parserrss.models.imageProps.FeedListIconProps;
import ru.iandreyshev.parserrss.models.imageProps.FullImage;
import ru.iandreyshev.parserrss.presentation.view.IImageView;
import ru.iandreyshev.parserrss.ui.adapter.IFeedItem;
import ru.iandreyshev.parserrss.ui.listeners.IItemImageRequestListener;

@InjectViewState
public class ImagesLoadPresenter extends MvpPresenter<IImageView> implements IItemImageRequestListener {
    public static final String TAG = "ImagesLoadPresenter";
    private final HashSet<Long> mIconsQueue = new HashSet<>();

    public void loadImage(long articleId) {
        GetArticleImageTask.execute(articleId, new GetImageFromNetListener(), FullImage.newInstance());
    }

    @Override
    public void getIconForItem(final IFeedItem item, boolean isWithoutQueue) {
        final Long id = item.getId();

        if (mIconsQueue.contains(id) && !isWithoutQueue) {
            return;
        } else if (item.isImageLoaded() && !isWithoutQueue) {
            return;
        }

        GetImageFromNetForFeedItem listener = new GetImageFromNetForFeedItem(mIconsQueue, item, id);
        GetArticleImageTask.execute(id, listener, FeedListIconProps.newInstance());
    }

    private class GetImageFromNetListener implements ITaskListener<Bitmap> {
        @Override
        public void onPreExecute() {
        }

        @Override
        public void onPostExecute(Bitmap result) {
            if (result != null) {
                getViewState().insertImage(result);
            }
        }
    }

    private static class GetImageFromNetForFeedItem implements ITaskListener<Bitmap> {
        private final long mIdBeforeLoad;
        private final WeakReference<IFeedItem> mItem;
        private final HashSet<Long> mQueue;

        private GetImageFromNetForFeedItem(HashSet<Long> queue, IFeedItem item, long articleId) {
            mIdBeforeLoad = articleId;
            mQueue = queue;
            mItem = new WeakReference<>(item);
        }

        @Override
        public void onPreExecute() {
            mQueue.add(mIdBeforeLoad);
        }

        @Override
        public void onPostExecute(Bitmap result) {
            mQueue.remove(mIdBeforeLoad);

            if (result != null && mItem.get() != null && mItem.get().getId() == mIdBeforeLoad) {
                mItem.get().updateImage(result);
            }
        }
    }
}
