package ru.iandreyshev.parserrss.presentation.presenter;

import ru.iandreyshev.parserrss.models.async.DeleteFeedTask;
import ru.iandreyshev.parserrss.models.async.GetNewRssTask;
import ru.iandreyshev.parserrss.models.async.UpdateRssTask;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.IViewRssArticle;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public final class FeedPresenter extends MvpPresenter<IFeedView> {
    private final static String TAG = FeedPresenter.class.getName();

    public void onUpdateRss(final IViewRss feed) {
        UpdateRssTask.execute(getViewState(), feed);
    }

    public void onSubmitInsertRss(final String url) {
        GetNewRssTask.execute(getViewState(), url);
    }

    public void onDeleteRss(final IViewRss rss) {
        DeleteFeedTask.execute(getViewState(), rss);
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
    }
}
