package ru.iandreyshev.parserrss.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.async.UpdateRssFromNetTask;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult;
import ru.iandreyshev.parserrss.presentation.view.IFeedTabView;

@InjectViewState
public class FeedTabPresenter extends MvpPresenter<IFeedTabView> {
    private IViewRss mRss;

    public void init(final IViewRss rss) {
        if (mRss != null) {
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
        @Override
        public void onPreExecute() {
            getViewState().startUpdate(true);
        }

        @Override
        public void onPostExecute(final IViewRss result) {
            getViewState().startUpdate(false);
        }

        @Override
        public void onRssNotExist() {
            getViewState().showShortToast(App.getStr(R.string.toast_rss_not_exist));
        }

        @Override
        public void onDatabaseError() {
            getViewState().showShortToast(App.getStr(R.string.toast_error_saving_to_db));
        }

        @Override
        public void onInvalidUrl() {
            getViewState().showShortToast(App.getStr(R.string.toast_invalid_url));
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
        public void onParserError() {
            getViewState().showShortToast(App.getStr(R.string.toast_invalid_rss_format));
        }

        @Override
        public void onSuccess(final IViewRss result) {
            getViewState().setArticles(result.getViewArticles());
        }
    }
}
