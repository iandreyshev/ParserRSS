package ru.iandreyshev.parserrss.presentation.presenter;

import android.util.Log;

import ru.iandreyshev.parserrss.models.rss.IRssArticle;
import ru.iandreyshev.parserrss.models.rss.IRssFeed;
import ru.iandreyshev.parserrss.models.async.InsertRssTask;
import ru.iandreyshev.parserrss.models.async.UpdateRssTask;
import ru.iandreyshev.parserrss.models.async.listener.IOnCancelledListener;
import ru.iandreyshev.parserrss.models.async.listener.IOnErrorListener;
import ru.iandreyshev.parserrss.models.async.listener.IOnSuccessListener;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public final class FeedPresenter extends MvpPresenter<IFeedView> {
    public void onRefreshFeed(IRssFeed feed) {
    }

    public void onSubmitAddingFeed(final String url) {
        final InsertRssTask task = new InsertRssTask();
        final InsertTaskListener listener = new InsertTaskListener();

        task.setSuccessListener(listener)
                .setErrorListener(listener)
                .setOnCancelledListener(listener)
                .execute(url);

        getViewState().startProgressBar(true);
    }

    public void onItemClick(IRssArticle article) {
        getViewState().openArticle(article);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    private class RefreshTaskListener
            implements
            IOnSuccessListener<Rss>,
            IOnErrorListener<UpdateRssTask.ErrorState> {
        @Override
        public void onSuccessEvent(Rss rss) {
            getViewState().updateFeedList(rss);
        }

        @Override
        public void onErrorEvent(UpdateRssTask.ErrorState refreshError) {

            switch (refreshError) {

                case InternetPermissionDenied:
                    getViewState().showShortToast("Internet permission denied");
                    break;
                case BadConnection:
                    getViewState().showShortToast("Bad connection");
                    break;
                case InvalidFormat:
                    getViewState().showShortToast("Parsing error");
                    break;
            }
        }
    }

    private class InsertTaskListener
            implements
            IOnSuccessListener<Rss>,
            IOnErrorListener<InsertRssTask.ErrorState>,
            IOnCancelledListener {
        @Override
        public void onSuccessEvent(Rss rss) {
            getViewState().startProgressBar(false);
            getViewState().insertFeed(rss);
            Log.e("InsertTaskListener", rss.getFeed().getTitle());
        }
        @Override
        public void onErrorEvent(InsertRssTask.ErrorState status) {
            getViewState().startProgressBar(false);

            switch (status) {
                case InternetPermissionDenied:
                    getViewState().showShortToast("Internet permission denied");
                    break;

                case InvalidUrl:
                    getViewState().showShortToast("Invalid url");
                    break;

                case BadConnection:
                    getViewState().showShortToast("Bad connection");
                    break;

                case InvalidFormat:
                    getViewState().showShortToast("Parsing error");
                    break;
            }
        }
        @Override
        public void onCancel() {
            getViewState().startProgressBar(false);
            getViewState().showLongToast("Cancel!");
        }
    }
}
