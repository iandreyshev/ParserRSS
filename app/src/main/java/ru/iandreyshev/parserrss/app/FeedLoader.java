package ru.iandreyshev.parserrss.app;

import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler;
import ru.iandreyshev.parserrss.models.web.Url;

public class FeedLoader {
    private Rss mRss;
    private State mStatus;

    public enum State {
        Success,
        InvalidUrl,
        BadConnection,
        InternetPermissionDenied,
        InvalidFormat,
    }

    public void load(final String urlStr) {
        final Url url = Url.parse(urlStr);

        if (url == null) {
            mStatus = State.InvalidUrl;

            return;
        }

        load(url);
    }

    public void load(final Url url) {
        final String rssText = takeRssFromNet(url);
        parseRss(rssText);
    }

    public State getState() {
        return mStatus;
    }

    public Rss getRss() {
        return mRss;
    }

    private String takeRssFromNet(final Url url) {
        final HttpRequestHandler requester = new HttpRequestHandler();
        requester.sendGet(url);

        switch (requester.getState()) {
            case Success:
                return new String(requester.getResponseBody());

            case BadUrl:
                mStatus = State.InvalidUrl;
                break;

            case BadConnection:
                mStatus = State.BadConnection;
                break;

            case PermissionDenied:
                mStatus = State.InternetPermissionDenied;
                break;
        }

        return null;
    }

    private void parseRss(final String rssText) {
        Rss rss = Rss.parse(rssText);

        if (rss == null) {
            mStatus = State.InvalidFormat;

            return;
        }

        mRss = rss;
        mStatus = State.Success;
    }
}
