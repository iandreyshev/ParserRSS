package ru.iandreyshev.parserrss.presentation.presenter;

import android.graphics.Bitmap;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.iandreyshev.parserrss.models.async.GetArticleImageTask;
import ru.iandreyshev.parserrss.models.async.ITaskListener;
import ru.iandreyshev.parserrss.presentation.view.IImageView;

@InjectViewState
public class ImagesLoadPresenter extends MvpPresenter<IImageView> {
    private long mArticleId;

    public void loadImage(final long articleId) {
        mArticleId = articleId;
        GetArticleImageTask.execute(mArticleId, new GetImageFromNetListener());
    }

    private class GetImageFromNetListener implements ITaskListener<Bitmap> {
        @Override
        public void onPreExecute() {
        }

        @Override
        public void onPostExecute(Bitmap result) {
            if (result != null) {
                getViewState().insertImage(mArticleId, result);
            }
        }
    }
}
