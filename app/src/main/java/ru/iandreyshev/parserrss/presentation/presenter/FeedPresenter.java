package ru.iandreyshev.parserrss.presentation.presenter;

import ru.iandreyshev.parserrss.models.async.DeleteFeedTask;
import ru.iandreyshev.parserrss.models.async.GetNewRssTask;
import ru.iandreyshev.parserrss.models.async.UpdateRssTask;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.rss.RssArticle;
import ru.iandreyshev.parserrss.models.rss.RssFeed;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public final class FeedPresenter extends MvpPresenter<IFeedView> {
    private final static String TAG = FeedPresenter.class.getName();

    public void onUpdateRss(final RssFeed feed) {
        UpdateRssTask.execute(getViewState(), feed);
    }

    public void onSubmitInsertRss(final String url) {
        GetNewRssTask.execute(getViewState(), url);
    }

    public void onDeleteRss(final Rss rss) {
        DeleteFeedTask.execute(getViewState(), rss);
    }

    public void openArticle(final RssArticle article) {
        getViewState().openArticle(article);
    }

    public void openRssInfo(final RssFeed feed) {
        getViewState().openRssInfo(feed);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }
}
