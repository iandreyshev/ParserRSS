package ru.iandreyshev.parserrss.presentation.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.iandreyshev.parserrss.models.async.UpdateRssFromNetTask;
import ru.iandreyshev.parserrss.models.rss.ViewRss;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;
import ru.iandreyshev.parserrss.presentation.view.IFeedTabView;

@InjectViewState
public class FeedTabPresenter extends MvpPresenter<IFeedTabView> {
    private static final String TAG = FeedTabPresenter.class.getName();

    private ViewRss mRss;

    public void init(final ViewRss rss) {
        if (rss == null) {
            return;
        }

        mRss = rss;
    }

    public void onUpdate() {
        UpdateRssFromNetTask.execute(new UpdateFromNetListener(), mRss.getUrl());
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().setArticles(mRss.getViewArticles());
    }

    private class UpdateFromNetListener implements UpdateRssFromNetTask.IEventListener {
        private static final String BAD_CONNECTION = "Connection error";
        private static final String BAD_URL = "Invalid url";
        private static final String NET_PERMISSION_DENIED = "Internet permission denied";
        private static final String PARSER_ERROR = "Invalid rss format";
        private static final String DATABASE_ERROR = "Saving error";
        private static final String RSS_WAS_DELETED = "Rss not exist";

        @Override
        public void onPreExecute() {
            getViewState().startUpdate(true);
        }

        @Override
        public void onPostExecute(final ViewRss result) {
            Log.e(TAG, "Stop update");
            getViewState().startUpdate(false);
        }

        @Override
        public void onRssNotExist() {
            getViewState().showShortToast(RSS_WAS_DELETED);
        }

        @Override
        public void onDatabaseError() {
            getViewState().showShortToast(DATABASE_ERROR);
        }

        @Override
        public void onInvalidUrl() {
            getViewState().showShortToast(BAD_URL);
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
        public void onParserError() {
            getViewState().showShortToast(PARSER_ERROR);
        }

        @Override
        public void onSuccess(final ViewRss result) {
            getViewState().setArticles(result.getViewArticles());
        }
    }
}
