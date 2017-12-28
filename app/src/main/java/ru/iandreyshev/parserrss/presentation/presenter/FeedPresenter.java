package ru.iandreyshev.parserrss.presentation.presenter;

import ru.iandreyshev.parserrss.models.async.DeleteRssFromDbTask;
import ru.iandreyshev.parserrss.models.async.GetAllRssFromDbTask;
import ru.iandreyshev.parserrss.models.async.InsertNewRssTask;
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
    private long mProgressBarUserCount;

    public void onInsertRss(final String url) {
        InsertNewRssTask.execute(new InsertRssFromNetListener(), url);
    }

    public void onDeleteRss(final ViewRss rss) {
        DeleteRssFromDbTask.execute(new DeletingRssListener(), rss);
    }

    public void openArticle(final ViewRssArticle article) {
        getViewState().openArticle(article);
    }

    public void openRssInfo(final ViewRss rss) {
        if (rss == null) {
            return;
        }

        getViewState().openRssInfo(rss);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        GetAllRssFromDbTask.execute(new LoadFromDatabaseListener());
    }

    private void startProgressBar(boolean isStart) {
        mProgressBarUserCount += (isStart) ? 1 : -1;
        getViewState().startProgressBar(mProgressBarUserCount > 0);
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

    private class InsertRssFromNetListener implements InsertNewRssTask.IEventListener {
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
        public void onParserError() {
            getViewState().showShortToast(INVALID_RSS_FORMAT);
        }

        @Override
        public void onNetError(final IHttpRequestResult requestResult) {
            switch (requestResult.getState()) {

                case BadConnection:
                    getViewState().showShortToast(BAD_CONNECTION);
                    break;

                case PermissionDenied:
                    getViewState().showShortToast(NET_PERMISSION_DENIED);
                    break;
            }
        }

        @Override
        public void onRssAlreadyExist() {
            getViewState().showShortToast(DUPLICATE_ERROR);
        }

        @Override
        public void onDatabaseError() {
            getViewState().showShortToast(DATABASE_ERROR);
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
        public void onPostExecute(final ViewRss result) {
            startProgressBar(false);
        }
    }

    private class LoadFromDatabaseListener implements GetAllRssFromDbTask.IEventListener {
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
