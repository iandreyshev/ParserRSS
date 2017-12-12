package ru.iandreyshev.parserrss.presentation.presenter;

import android.os.AsyncTask;

import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;
import ru.iandreyshev.parserrss.presentation.presenter.task.InsertFeedTask;
import ru.iandreyshev.parserrss.presentation.presenter.task.RefreshFeedTask;
import ru.iandreyshev.parserrss.presentation.presenter.task.listeners.IOnErrorListener;
import ru.iandreyshev.parserrss.presentation.presenter.task.listeners.IOnSuccessListener;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

@InjectViewState
public final class FeedPresenter extends MvpPresenter<IFeedView> {
    private RefreshFeedTask mRefreshTask = new RefreshFeedTask();
    private InsertFeedTask mInsertTask = new InsertFeedTask();
    private IFeedInfo mFeed;

    public void onRefresh() {
    }

    public void onAddingButtonClick() {
        getViewState().openAddingFeedDialog();
    }

    public void onEditButtonClick() {
    }

    public void onSubmitAddingFeed(final String url) {
        System.out.println("URL: " + url);

        if (!mInsertTask.isCancelled() && mInsertTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        }

        mInsertTask = new InsertFeedTask();

        System.out.println("Start task");
        InsertTaskListener listener = new InsertTaskListener();

        mInsertTask.setSuccessListener(listener)
                .setErrorListener(listener)
                .execute(url);

        getViewState().startProgressBar(true);
    }

    public void onItemClick(IArticleInfo article) {
        getViewState().openArticle(article);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    private void onRefresh(List<IFeedInfo> feedsToRefresh) {
        if (mRefreshTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        } else if (mRefreshTask.getStatus() == AsyncTask.Status.FINISHED || mRefreshTask.isCancelled()) {
            mRefreshTask = new RefreshFeedTask();
        }

        final IFeedInfo[] feeds = feedsToRefresh.toArray(new IFeedInfo[0]);
        final RefreshTaskListener listener = new RefreshTaskListener();

        mRefreshTask.setSuccessListener(listener)
                .setErrorListener(listener)
                .execute(feeds);
    }

    private class RefreshTaskListener
            implements
            IOnErrorListener<RefreshFeedTask.Status>,
            IOnSuccessListener<List<IArticleInfo>> {
        @Override
        public void onSuccessEvent(List<IArticleInfo> result) {

        }

        @Override
        public void onErrorEvent(RefreshFeedTask.Status refreshError) {

        }
    }

    private class InsertTaskListener
            implements
            IOnSuccessListener<IFeedInfo>,
            IOnErrorListener<InsertFeedTask.Status> {
        @Override
        public void onSuccessEvent(IFeedInfo feedInfo) {
            getViewState().startProgressBar(false);
            System.out.println(feedInfo.getTitle());
        }

        @Override
        public void onErrorEvent(InsertFeedTask.Status status) {
            getViewState().startProgressBar(false);
            System.out.println(status);
        }
    }
}
