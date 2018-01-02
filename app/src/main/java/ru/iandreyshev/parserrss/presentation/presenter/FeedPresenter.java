package ru.iandreyshev.parserrss.presentation.presenter;

import android.support.annotation.NonNull;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.async.DeleteRssFromDbTask;
import ru.iandreyshev.parserrss.models.async.GetAllRssFromDbTask;
import ru.iandreyshev.parserrss.models.async.InsertNewRssTask;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

@InjectViewState
public final class FeedPresenter extends MvpPresenter<IFeedView> {
    private long mProgressBarUserCount;

    public void onInsertRss(final String url) {
        InsertNewRssTask.execute(new InsertRssFromNetListener(), url);
    }

    public void onDeleteRss(final IViewRss rss) {
        DeleteRssFromDbTask.execute(new DeletingRssListener(), rss);
    }

    public void openArticle(final IViewArticle article) {
        getViewState().openArticle(article);
    }

    public void openRssInfo(@NonNull final IViewRss rss) {
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
        @Override
        public void onFail(IViewRss rss) {
            getViewState().showShortToast(App.getStr(R.string.toast_error_deleting_from_db));
        }

        @Override
        public void onSuccess(IViewRss rss) {
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

    private class InsertRssFromNetListener implements InsertNewRssTask.IEventListener {
        @Override
        public void onInvalidUrl() {
            getViewState().showShortToast(App.getStr(R.string.toast_invalid_url));
        }

        @Override
        public void onParserError() {
            getViewState().showShortToast(App.getStr(R.string.toast_invalid_rss_format));
        }

        @Override
        public void onNetError(final IHttpRequestResult requestResult) {
            switch (requestResult.getState()) {

                case BadConnection:
                    getViewState().showShortToast(App.getStr(R.string.toast_bad_connection));
                    break;

                case PermissionDenied:
                    getViewState().showShortToast(App.getStr(R.string.toast_internet_permission_denied));
                    break;
            }
        }

        @Override
        public void onRssAlreadyExist() {
            getViewState().showShortToast(App.getStr(R.string.toast_rss_already_exist));
        }

        @Override
        public void onDatabaseError() {
            getViewState().showShortToast(App.getStr(R.string.toast_error_saving_to_db));
        }

        @Override
        public void onSuccess(final IViewRss rss) {
            getViewState().insertRss(rss);
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

    private class LoadFromDatabaseListener implements GetAllRssFromDbTask.IEventListener {
        @Override
        public void onLoadError() {
            getViewState().showShortToast(App.getStr(R.string.toast_error_loading_from_db));
        }

        @Override
        public void onSuccess(final List<IViewRss> rssFromDb) {
            for (final IViewRss rss : rssFromDb) {
                getViewState().insertRss(rss);
            }
        }

        @Override
        public void onPreExecute() {
            startProgressBar(true);
        }

        @Override
        public void onPostExecute(List<IViewRss> result) {
            startProgressBar(false);
        }
    }
}
