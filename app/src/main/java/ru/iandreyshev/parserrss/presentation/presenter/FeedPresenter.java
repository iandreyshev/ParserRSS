package ru.iandreyshev.parserrss.presentation.presenter;

import android.os.AsyncTask;

import ru.iandreyshev.parserrss.models.article.IArticleContent;
import ru.iandreyshev.parserrss.models.feed.Feed;
import ru.iandreyshev.parserrss.models.feed.IFeedContent;
import ru.iandreyshev.parserrss.presentation.presenter.task.IFeedTask;
import ru.iandreyshev.parserrss.presentation.presenter.task.InsertFeedTask;
import ru.iandreyshev.parserrss.presentation.presenter.task.RefreshFeedTask;
import ru.iandreyshev.parserrss.presentation.presenter.task.listener.IOnCancelledListener;
import ru.iandreyshev.parserrss.presentation.presenter.task.listener.IOnErrorListener;
import ru.iandreyshev.parserrss.presentation.presenter.task.listener.IOnSuccessListener;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;

@InjectViewState
public final class FeedPresenter extends MvpPresenter<IFeedView> {
    private static final String TAG = "FeedPresenter";

    private RefreshFeedTask mRefreshTask = new RefreshFeedTask();
    private InsertFeedTask mInsertTask = new InsertFeedTask();

    private Feed mFeed;

    public void onRefreshFeed() {
        if (mFeed == null) {
            getViewState().setRefreshing(false);

            return;
        }

        onRefreshFeed(mFeed);
    }

    public void onAddingButtonClick() {
        getViewState().openAddingFeedDialog();
    }

    public void onEditButtonClick() {
    }

    public void onSubmitAddingFeed(final String url) {
        if (mInsertTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        }

        mInsertTask = new InsertFeedTask();
        final InsertTaskListener listener = new InsertTaskListener();

        mInsertTask.setSuccessListener(listener)
                .setErrorListener(listener)
                .setOnCancelledListener(listener)
                .execute(url);

        getViewState().startProgressBar(true);
    }

    public void onItemClick(IArticleContent article) {
        getViewState().openArticle(article);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    private void onRefreshFeed(IFeedContent feedToRefresh) {
        if (feedToRefresh == null) {
            return;
        }

        if (mRefreshTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        } else if (mRefreshTask.getStatus() == AsyncTask.Status.FINISHED || mRefreshTask.isCancelled()) {
            mRefreshTask = new RefreshFeedTask();
        }

        final RefreshTaskListener listener = new RefreshTaskListener();

        mRefreshTask.setSuccessListener(listener)
                .setErrorListener(listener)
                .execute(mFeed);
    }

    private class RefreshTaskListener
            implements IOnSuccessListener<IFeedTask>, IOnErrorListener<RefreshFeedTask.ErrorStatus> {
        @Override
        public void onSuccessEvent(IFeedTask task) {
            getViewState().setRefreshing(false);
            getViewState().updateFeedList(task.getFeed(), new ArrayList<>(task.getArticles()));
        }

        @Override
        public void onErrorEvent(RefreshFeedTask.ErrorStatus refreshError) {
            getViewState().setRefreshing(false);

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
            implements IOnSuccessListener<IFeedTask>,
            IOnErrorListener<InsertFeedTask.ErrorStatus>,
            IOnCancelledListener {
        @Override
        public void onSuccessEvent(IFeedTask task) {
            getViewState().startProgressBar(false);

            mFeed = task.getFeed();
            getViewState().setFeed(mFeed);
            getViewState().updateFeedList(mFeed, new ArrayList<>(task.getArticles()));
        }
        @Override
        public void onErrorEvent(InsertFeedTask.ErrorStatus status) {
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
