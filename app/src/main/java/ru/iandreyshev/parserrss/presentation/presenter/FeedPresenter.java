package ru.iandreyshev.parserrss.presentation.presenter;

import ru.iandreyshev.parserrss.models.async.ITaskListener;
import ru.iandreyshev.parserrss.models.async.InsertRssTask;
import ru.iandreyshev.parserrss.models.async.UpdateRssTask;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.rss.RssArticle;
import ru.iandreyshev.parserrss.models.rss.RssFeed;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public final class FeedPresenter extends MvpPresenter<IFeedView> {
    public void onRefreshRss(final RssFeed feed) {
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

    public void onArticleClick(final RssArticle article) {
        getViewState().openArticle(article);
    }

    public void onOpenInfo(final RssFeed feed) {
        getViewState().openRssInfo(feed);
    }

    public void onDeleteFeed(final RssFeed feed) {
        getViewState().removeRss(feed);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    private class InsertTaskListener implements ITaskListener<String, Void, Rss, InsertRssTask.ErrorState> {
        @Override
        public void onSuccessEvent(Rss rss) {
            getViewState().startProgressBar(false);
            getViewState().insertRss(rss);
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
            getViewState().showLongToast("Cancel");
        }
    }

    private class UpdateTaskListener implements ITaskListener<RssFeed, Void, Rss, UpdateRssTask.ErrorState> {
        @Override
        public void onSuccessEvent(Rss rss) {
            getViewState().updateArticles(rss);
        }

        @Override
        public void onCancel(RssFeed[] feeds) {
            getViewState().startRefresh(feeds[0], false);
        }

        @Override
        public void onErrorEvent(RssFeed[] feeds, UpdateRssTask.ErrorState refreshError) {
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
            getViewState().startRefresh(feeds[0], false);
        }

        @Override
        public void onProgress(Void[] process) {
        }
    }

    private class DeleteTaskListener implements ITaskListener<RssFeed, Void, Void, Void> {
        @Override
        public void onCancel(RssFeed[] feeds) {
        }

        @Override
        public void onErrorEvent(RssFeed[] feeds, Void aVoid) {
        }

        @Override
        public void onProgress(Void[] process) {
        }

        @Override
        public void onSuccessEvent(Void aVoid) {
        }
    }
}
