package ru.iandreyshev.parserrss.presentation.presenter;

import android.util.Log;

import ru.iandreyshev.parserrss.models.async.ITaskListener;
import ru.iandreyshev.parserrss.models.rss.IRssArticle;
import ru.iandreyshev.parserrss.models.rss.IRssFeed;
import ru.iandreyshev.parserrss.models.async.InsertRssTask;
import ru.iandreyshev.parserrss.models.async.UpdateRssTask;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public final class FeedPresenter extends MvpPresenter<IFeedView> {
    public void onRefreshFeed(IRssFeed feed) {
        Log.e("FeedPresenter", "Start refresh");
        getViewState().startRefresh(feed, true);

        new UpdateRssTask()
                .setListener(new UpdateTaskListener())
                .execute(feed);
    }

    public void onSubmitAddingFeed(final String url) {
        final InsertRssTask task = new InsertRssTask();

        task.setListener(new InsertTaskListener())
                .execute(url);

        getViewState().startProgressBar(true);
    }

    public void onArticleClick(IRssArticle article) {
        getViewState().openArticle(article);
    }

    public void onOpenInfo(IRssFeed feed) {
        if (feed == null) {
            return;
        }
    }

    public void onDeleteFeed(final Rss rss) {
        getViewState().removeRss(rss);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    private class InsertTaskListener implements ITaskListener<String, Void, Rss, InsertRssTask.ErrorState> {
        @Override
        public void onSuccessEvent(Rss rss) {
            getViewState().startProgressBar(false);
            getViewState().insertFeed(rss);
        }

        @Override
        public void onErrorEvent(String[] urls, InsertRssTask.ErrorState status) {
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
        public void onProgress(Void[] process) {
        }

        @Override
        public void onCancel(String[] urlsCollection) {
            getViewState().startProgressBar(false);
            getViewState().showLongToast("Cancel!");
        }
    }

    private class UpdateTaskListener implements ITaskListener<IRssFeed, Void, Rss, UpdateRssTask.ErrorState> {
        @Override
        public void onSuccessEvent(Rss rss) {
            Log.e("Task", "Refreshing success");
            getViewState().updateFeedList(rss);
        }

        @Override
        public void onCancel(IRssFeed[] feeds) {
            Log.e("Task", "Refreshing cancelled");
            stopRefreshFirst(feeds, 0);
        }

        @Override
        public void onErrorEvent(IRssFeed[] feeds, UpdateRssTask.ErrorState refreshError) {
            Log.e("Task", "Refreshing error");
            switch (refreshError) {
                case InternetPermissionDenied:
                    getViewState().showShortToast("Internet permission denied");
                    break;
                case BadConnection:
                    getViewState().showShortToast("Bad connection");
                    break;
                case InvalidUrl:
                    getViewState().showShortToast("Invalid url");
                    break;
                case InvalidFormat:
                    getViewState().showShortToast("Parsing error");
                    break;
            }
            stopRefreshFirst(feeds, 0);
        }

        @Override
        public void onProgress(Void[] process) {
        }

        private void stopRefreshFirst(IRssFeed[] feeds, int index) {
            if (feeds == null || feeds.length <= index) {
                return;
            }

            getViewState().startRefresh(feeds[index], false);
        }
    }

    private class DeleteTaskListener implements ITaskListener<IRssFeed, Void, Void, Void> {
        @Override
        public void onCancel(IRssFeed[] feeds) {
        }

        @Override
        public void onErrorEvent(IRssFeed[] feeds, Void aVoid) {
        }

        @Override
        public void onProgress(Void[] process) {
        }

        @Override
        public void onSuccessEvent(Void aVoid) {
        }
    }
}
