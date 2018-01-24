package ru.iandreyshev.parserrss.presentation.presenter;

import android.support.annotation.NonNull;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.async.DeleteRssFromDbTask;
import ru.iandreyshev.parserrss.models.async.GetAllRssFromDbTask;
import ru.iandreyshev.parserrss.models.async.InsertNewRssTask;
import ru.iandreyshev.parserrss.models.filters.FilterByDate;
import ru.iandreyshev.parserrss.models.rss.ViewRss;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

@InjectViewState
public final class FeedPresenter extends MvpPresenter<IFeedView> {
    private long mProgressBarUserCount;

    public void onInsertRss(final String url) {
        InsertNewRssTask.execute(new InsertRssFromNetListener(), url, FilterByDate.Companion.newInstance());
    }

    public void onDeleteRss(final ViewRss rss) {
        DeleteRssFromDbTask.execute(new DeletingRssListener(), rss);
        getViewState().removeRss(rss);
    }

    public void openArticle(long articleId) {
        getViewState().openArticle(articleId);
    }

    public void openRssInfo(@NonNull final ViewRss rss) {
        getViewState().openRssInfo(rss);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        GetAllRssFromDbTask.execute(new LoadFromDatabaseListener(), FilterByDate.Companion.newInstance());
    }

    private void startProgressBar(boolean isStart) {
        mProgressBarUserCount += (isStart) ? 1 : -1;
        getViewState().startProgressBar(mProgressBarUserCount > 0);
    }

    private class DeletingRssListener implements DeleteRssFromDbTask.IEventListener {
        @Override
        public void onFail(ViewRss rss) {
            getViewState().showShortToast(App.getStr(R.string.toast_error_deleting_from_db));
        }

        @Override
        public void onPreExecute() {
        }

        @Override
        public void onPostExecute(final ViewRss result) {
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
        public void onSuccess(final ViewRss rss) {
            getViewState().insertRss(rss);
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
        @Override
        public void onLoadError() {
            getViewState().showShortToast(App.getStr(R.string.toast_error_loading_from_db));
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
