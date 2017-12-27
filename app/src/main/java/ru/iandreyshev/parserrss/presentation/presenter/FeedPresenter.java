package ru.iandreyshev.parserrss.presentation.presenter;

import ru.iandreyshev.parserrss.models.async.DeleteRssFromDbTask;
import ru.iandreyshev.parserrss.models.async.GetRssFromDbTask;
import ru.iandreyshev.parserrss.models.async.GetRssFromNetTask;
import ru.iandreyshev.parserrss.models.async.UpdateRssFromNetTask;
import ru.iandreyshev.parserrss.models.rss.ViewRss;
import ru.iandreyshev.parserrss.models.rss.ViewRssArticle;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

@InjectViewState
public final class FeedPresenter extends MvpPresenter<IFeedView> {
    private final static String TAG = FeedPresenter.class.getName();
    private long mProgressBarUsers;

    public void onInsertRss(final String url) {
        GetRssFromNetTask.execute(new GetRssFromNetListener(), url);
    }

    public void onUpdateRss(final ViewRss rss) {
        UpdateRssFromNetTask.execute(new UpdateRssFromNetListener(), rss);
    }

    public void onDeleteRss(final ViewRss rss) {
        DeleteRssFromDbTask.execute(new DeletingRssListener(), rss);
    }

    public void onMenuOpen() {
        // TODO: Check enable buttons
    }

    public void openArticle(final ViewRssArticle article) {
        getViewState().openArticle(article);
    }

    public void openRssInfo(final ViewRss rss) {
        getViewState().openRssInfo(rss);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        GetRssFromDbTask.execute(new GetRssFromDbListener());
    }

    private void startProgressBar(boolean isStart) {
        mProgressBarUsers += (isStart) ? 1 : -1;
        getViewState().startProgressBar(mProgressBarUsers > 0);
    }

    private class DeletingRssListener implements DeleteRssFromDbTask.IEventListener {
        private static final String ERROR = "Delete error";
        private static final String SUCCESS = "Deleting success";

        @Override
        public void onFail(ViewRss rss) {
            getViewState().showShortToast(ERROR);
        }

        @Override
        public void onSuccess(ViewRss rss) {
            getViewState().showShortToast(SUCCESS);
            getViewState().removeRss(rss);
        }

        @Override
        public void onPreExecute() {
            startProgressBar(true);
        }

        @Override
        public void onPostExecute(final ViewRss result) {
            startProgressBar(false);
        }
    }

    private class GetRssFromNetListener implements GetRssFromNetTask.IEventListener {
        private static final String BAD_URL = "Invalid url";
        private static final String BAD_CONNECTION = "Connection error";
        private static final String NET_PERMISSION_DENIED = "Internet permission denied";
        private static final String INVALID_RSS_FORMAT = "Invalid rss format";
        private static final String DATABASE_ERROR = "Saving error";
        private static final String DUPLICATE_ERROR = "Rss already exist";
        private static final String SUCCESS = "Rss inserted";

        @Override
        public void onInvalidUrl() {
            getViewState().showShortToast(BAD_URL);
        }

        @Override
        public void onNetError(final IHttpRequestResult requestResult) {
            switch (requestResult.getState()) {
                case BadUrl:
                    getViewState().showShortToast(BAD_URL);
                    break;
                case BadConnection:
                    getViewState().showShortToast(BAD_CONNECTION);
                    break;
                case PermissionDenied:
                    getViewState().showShortToast(NET_PERMISSION_DENIED);
                    break;
            }
        }

        @Override
        public void onParsingError() {
            getViewState().showShortToast(INVALID_RSS_FORMAT);
        }

        @Override
        public void onDbError() {
            getViewState().showShortToast(DATABASE_ERROR);
        }

        @Override
        public void onDuplicateRss() {
            getViewState().showShortToast(DUPLICATE_ERROR);
        }

        @Override
        public void onSuccess(final ViewRss rss) {
            getViewState().insertRss(rss);
            getViewState().showShortToast(SUCCESS);
        }

        @Override
        public void onPreExecute() {
            startProgressBar(true);
        }

        @Override
        public void onPostExecute(ViewRss result) {
            startProgressBar(false);
        }
    }

    private class UpdateRssFromNetListener implements UpdateRssFromNetTask.IEventListener {
        private static final String BAD_CONNECTION = "Connection error";
        private static final String BAD_URL = "Invalid url";
        private static final String NET_PERMISSION_DENIED = "Internet permission denied";
        private static final String INVALID_RSS_FORMAT = "Invalid rss format";
        private static final String SAVING_ERROR = "Saving error";
        private static final String RSS_WAS_DELETED = "Rss was deleted";

        @Override
        public void onNetError(final ViewRss rss, final IHttpRequestResult requestResult) {
            switch (requestResult.getState()) {
                case BadUrl:
                    getViewState().showShortToast(BAD_URL);
                    break;
                case BadConnection:
                    getViewState().showShortToast(BAD_CONNECTION);
                    break;
                case PermissionDenied:
                    getViewState().showShortToast(NET_PERMISSION_DENIED);
                    break;
            }
        }

        @Override
        public void onParsingError(final ViewRss rss) {
            getViewState().showShortToast(INVALID_RSS_FORMAT);
        }

        @Override
        public void onRssNotExist(final ViewRss rss) {
            getViewState().showShortToast(RSS_WAS_DELETED);
            getViewState().removeRss(rss);
        }

        @Override
        public void onDbError(final ViewRss rss) {
            getViewState().showShortToast(SAVING_ERROR);
        }

        @Override
        public void onSuccess(final ViewRss rss) {
            getViewState().updateArticles(rss);
        }

        @Override
        public void onPreExecute() {
        }

        @Override
        public void onPostExecute(ViewRss result) {
        }
    }

    private class GetRssFromDbListener implements GetRssFromDbTask.IEventListener {
        private static final String ERROR = "Loading error";

        @Override
        public void onLoadError() {
            getViewState().showShortToast(ERROR);
        }

        @Override
        public void onSuccess(final List<ViewRss> rssFromDb) {
            for (final ViewRss rss : rssFromDb) {
                getViewState().insertRss(rss);
            }
        }

        @Override
        public void onPreExecute() {
            startProgressBar(true);
        }

        @Override
        public void onPostExecute(List<ViewRss> result) {
            startProgressBar(false);
        }
    }
}
