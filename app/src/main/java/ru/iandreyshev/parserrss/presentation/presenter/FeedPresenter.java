package ru.iandreyshev.parserrss.presentation.presenter;

import ru.iandreyshev.parserrss.models.async.DeleteFeedTask;
import ru.iandreyshev.parserrss.models.async.GetRssFromDbTask;
import ru.iandreyshev.parserrss.models.async.GetRssFromNetTask;
import ru.iandreyshev.parserrss.models.async.UpdateRssTask;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.IViewRssArticle;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public final class FeedPresenter extends MvpPresenter<IFeedView> implements IFeedViewHost {
    private final static String TAG = FeedPresenter.class.getName();

    public void onUpdateRss(final IViewRss rss) {
        UpdateRssTask.execute(this, rss);
    }

    public void onSubmitInsertRss(final String url) {
        GetRssFromNetTask.execute(this, url);
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
        GetRssFromDbTask.execute(this);
    }
}
