package ru.iandreyshev.parserrss.presentation.presenter;

import ru.iandreyshev.parserrss.models.async.DeleteRssFromDbTask;
import ru.iandreyshev.parserrss.models.async.GetRssFromDbTask;
import ru.iandreyshev.parserrss.models.async.GetRssFromNetTask;
import ru.iandreyshev.parserrss.models.async.UpdateRssFromNetTask;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.IViewRssArticle;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;

@InjectViewState
public final class FeedPresenter extends MvpPresenter<IFeedView> {
    private final static String TAG = FeedPresenter.class.getName();
    private long mProgressBarUsers;

    public void onUpdateRss(final IViewRss rss) {
        UpdateRssFromNetTask.execute(new UpdateRssFromNetListener(), rss);
    }

    public void onInsertRss(final String url) {
        GetRssFromNetTask.execute(new GetRssFromNetListener(), url);
    }

    public void onDeleteRss(final IViewRss rss) {
        DeleteRssFromDbTask.execute(new DeletingRssListener(), rss);
    }

    public void onMenuOpen() {
    }

    public void openArticle(final IViewRssArticle article) {
        getViewState().openArticle(article);
    }

    public void openRssInfo(final IViewRss rss) {
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
        public void onFail(long count, IViewRss rss) {
            getViewState().showShortToast(ERROR);
        }

        @Override
        public void onSuccess(long count, IViewRss rss) {
            getViewState().showShortToast(SUCCESS);
            getViewState().removeRss(rss);
        }

        @Override
        public void onPreExecute() {
            startProgressBar(true);
        }

        @Override
        public void onPostExecute(final IViewRss result) {
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
        public void onSuccess(final IViewRss rss) {
            getViewState().insertRss(rss);
            getViewState().showShortToast(SUCCESS);
        }

        @Override
        public void onPreExecute() {
            startProgressBar(true);
        }

        @Override
        public void onPostExecute(IViewRss result) {
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
        public void onNetError(final IViewRss rss, final IHttpRequestResult requestResult) {
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
        public void onParsingError(final IViewRss rss) {
            getViewState().showShortToast(INVALID_RSS_FORMAT);
        }

        @Override
        public void onRssDeleted(final IViewRss rss) {
            getViewState().showShortToast(RSS_WAS_DELETED);
            getViewState().removeRss(rss);
        }

        @Override
        public void onDbError(final IViewRss rss) {
            getViewState().showShortToast(SAVING_ERROR);
        }

        @Override
        public void onSuccess(final IViewRss rss) {
            getViewState().updateArticles(rss);
        }

        @Override
        public void onPreExecute() {
        }

        @Override
        public void onPostExecute(IViewRss result) {
        }
    }

    private class GetRssFromDbListener implements GetRssFromDbTask.IEventListener {
        private static final String ERROR = "Loading error";

        @Override
        public void onLoadError() {
            getViewState().showShortToast(ERROR);
        }

        @Override
        public void onSuccess(final ArrayList<IViewRss> rssFromDb) {
            for (final IViewRss rss : rssFromDb) {
                getViewState().insertRss(rss);
            }
        }

        @Override
        public void onPreExecute() {
            startProgressBar(true);
        }

        @Override
        public void onPostExecute(ArrayList<IViewRss> result) {
            startProgressBar(false);
        }
    }
}
