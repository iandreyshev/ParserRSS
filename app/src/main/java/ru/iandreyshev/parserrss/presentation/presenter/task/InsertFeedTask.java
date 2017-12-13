package ru.iandreyshev.parserrss.presentation.presenter.task;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.app.FeedLoader;
import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.feed.Feed;
import ru.iandreyshev.parserrss.models.web.Url;

public class InsertFeedTask
        extends Task<String, Void, IFeedTask, InsertFeedTask.ErrorStatus>
        implements IFeedTask {

    private Feed mFeed;
    private List<Article> mArticles = new ArrayList<>();

    public enum ErrorStatus {
        InvalidUrl,
        BadConnection,
        InternetPermissionDenied,
        InvalidFormat,
    }

    @Override
    public List<Article> getArticles() {
        return mArticles;
    }

    @Override
    public Feed getFeed() {
        return mFeed;
    }

    @Override
    protected IFeedTask doInBackground(String... urlCollection) {
        Url url = Url.parse(urlCollection[0]);

        FeedLoader loader = new FeedLoader();
        loader.load(url);

        if (loader.getState() != FeedLoader.State.Success) {
            initErrorStatus(loader.getState());

            return this;
        }

        mFeed = loader.getRss().getFeed();
        mFeed.setUrl(url);

        mArticles.addAll(loader.getRss().getArticles());

        return this;
    }

    private void initErrorStatus(final FeedLoader.State loadStatus) {
        switch (loadStatus) {
            case InvalidUrl:
                setError(ErrorStatus.InvalidUrl);
                break;

            case BadConnection:
                setError(ErrorStatus.BadConnection);
                break;

            case InternetPermissionDenied:
                setError(ErrorStatus.InternetPermissionDenied);
                break;

            case InvalidFormat:
                setError(ErrorStatus.InvalidFormat);
                break;
        }
    }
}
