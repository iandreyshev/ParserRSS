package ru.iandreyshev.parserrss.presentation.presenter;

import android.graphics.Bitmap;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.iandreyshev.parserrss.models.async.GetImageFromNetTask;
import ru.iandreyshev.parserrss.models.async.ITaskListener;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;
import ru.iandreyshev.parserrss.presentation.view.IFeedTabView;
import ru.iandreyshev.parserrss.ui.listeners.IOnImageInsertListener;
import ru.iandreyshev.parserrss.ui.listeners.IOnImageRequestListener;

@InjectViewState
public class ImagesLoadPresenter extends MvpPresenter<IFeedTabView> implements IOnImageRequestListener {
    @Override
    public void loadImage(IViewArticle article, IOnImageInsertListener insertListener) {
        if (article.getImage() != null || article.getImageUrl() == null) {
            return;
        }

        GetImageFromNetTask.execute(article, new GetImageFromNetListener(insertListener));
    }

    private class GetImageFromNetListener implements ITaskListener<Bitmap> {
        IOnImageInsertListener mOnInsertListener;

        GetImageFromNetListener(final IOnImageInsertListener listener) {
            mOnInsertListener = listener;
        }

        @Override
        public void onPreExecute() {
        }

        @Override
        public void onPostExecute(Bitmap result) {
            if (result != null) {
                mOnInsertListener.insert(result);
            }
        }
    }
}
