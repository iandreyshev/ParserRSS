package ru.iandreyshev.parserrss.app;

import android.support.annotation.NonNull;

import java.util.List;

import okhttp3.HttpUrl;
import ru.iandreyshev.parserrss.app.parserRss.ParserRss;
import ru.iandreyshev.parserrss.app.parserRss.ParserRssResult;
import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.feed.Feed;

public class FeedLoader {
    private Feed mFeed;
    private List<Article> mArticles;
    private Status mStatus;

    public enum Status {
        Success,
        InvalidUrl,
        BadConnection,
        InternetPermissionDenied,
        InvalidFormat,
    }

    public void load(final String urlStr) {
        final HttpUrl url = HttpUrl.parse(urlStr);

        if (url == null) {
            mStatus = Status.InvalidUrl;

            return;
        }

        load(url);
    }

    public void load(final HttpUrl url) {
        final String rssText = takeRssFromNet(url);

        if (rssText == null) {
            return;
        }

        parseRss(rssText);
    }

    public Status getStatus() {
        return mStatus;
    }

    public List<Article> getArticles() {
        return mArticles;
    }

    public Feed getFeed() {
        return mFeed;
    }

    private String takeRssFromNet(final HttpUrl url) {
        final HttpRequestHandler requester = new HttpRequestHandler();
        requester.sendGet(url);

        switch (requester.getStatus()) {
            case Success:
                return new String(requester.getResponseBody());

            case BadUrl:
                mStatus = Status.InvalidUrl;
                break;

            case BadConnection:
                mStatus = Status.BadConnection;
                break;

            case PermissionDenied:
                mStatus = Status.InternetPermissionDenied;
                break;
        }

        return null;
    }

    private void parseRss(final String rss) {
        ParserRss parser = new ParserRss();
        parser.parse(rss);

        if (parser.getResult() != ParserRssResult.Success) {
            mStatus = Status.InvalidFormat;

            return;
        }

        mStatus = Status.Success;
        mFeed = parser.getFeed();
        mArticles = parser.getArticles();
    }
}
