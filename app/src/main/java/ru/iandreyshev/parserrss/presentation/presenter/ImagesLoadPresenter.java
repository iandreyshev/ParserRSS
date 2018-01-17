package ru.iandreyshev.parserrss.presentation.presenter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.HashSet;

import ru.iandreyshev.parserrss.models.async.GetArticleImageTask;
import ru.iandreyshev.parserrss.presentation.view.IImageView;
import ru.iandreyshev.parserrss.ui.adapter.IFeedItem;
import ru.iandreyshev.parserrss.ui.listeners.IItemImageRequestListener;

@InjectViewState
public class ImagesLoadPresenter extends MvpPresenter<IImageView> implements IItemImageRequestListener {
    private HashSet<Long> mItemsQueue = new HashSet<>();

    public void loadImage(long articleId) {
        GetArticleImageTask.execute(articleId, new GetImageFromNetListener());
    }

    @Override
    public void getImageFor(final IFeedItem item, boolean isForce) {
        long id = item.getArticle().getId();

        if (item.getArticle() == null) {
            return;
        } else if (mItemsQueue.contains(id) && !isForce) {
            return;
        } else if (item.isImageLoaded() && !isForce) {
            return;
        }

        GetArticleImageTask.execute(id, new GetImageFromNetForFeedItem(item));
    }

    private class GetImageFromNetListener implements GetArticleImageTask.IEventListener {
        @Override
        public void onPreExecute() {
        }

        @Override
        public void onPostExecute(Bitmap result) {
        }

        @Override
        public void onSuccess(@NonNull byte[] imageBytes, @NonNull Bitmap bitmap) {
            getViewState().insertImage(imageBytes, bitmap);
        }
    }

    private class GetImageFromNetForFeedItem implements GetArticleImageTask.IEventListener {
        private long mIdBeforeLoad;
        private IFeedItem mItem;

        private GetImageFromNetForFeedItem(IFeedItem item) {
            mIdBeforeLoad = item.getArticle().getId();
            mItem = item;
        }

        @Override
        public void onPreExecute() {
            mItemsQueue.add(mIdBeforeLoad);
        }

        @Override
        public void onPostExecute(Bitmap result) {
            mItemsQueue.remove(mIdBeforeLoad);
        }

        @Override
        public void onSuccess(@NonNull byte[] imageBytes, @NonNull Bitmap bitmap) {
            if (mItem.getArticle().getId() == mIdBeforeLoad) {
                mItem.getArticle().setImage(imageBytes);
                mItem.updateImage(bitmap);
            }
        }
    }
}
