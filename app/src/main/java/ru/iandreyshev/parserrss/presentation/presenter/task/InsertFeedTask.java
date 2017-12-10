package ru.iandreyshev.parserrss.presentation.presenter.task;

import ru.iandreyshev.parserrss.app.NetWorker;
import ru.iandreyshev.parserrss.app.ParserRSS;
import ru.iandreyshev.parserrss.models.feed.Feed;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;

public class InsertFeedTask extends Task<String, Void, IFeedInfo, InsertFeedTask.Status> {
    public enum Status {
        BadConnection,
        InvalidUrl,
        InvalidXmlFormat,
    }

    @Override
    protected IFeedInfo doInBackground(String... urlCollection) {
        if (urlCollection.length < 0) {
            cancel(Status.InvalidUrl);
        }

        final String url = urlCollection[0];
        final NetWorker newWorker = new NetWorker();
        newWorker.send(url);

        switch (newWorker.getStatus()) {
            case BadUrl:
                cancel(Status.BadConnection);
                break;
            case BadConnection:
                cancel(Status.InvalidUrl);
                break;
        }

        final ParserRSS parserRSS = new ParserRSS();
        parserRSS.parse(newWorker.getResponseAsText());

        if (!parserRSS.isSuccess()) {
            cancel(Status.InvalidXmlFormat);
        }

        return new Feed(0, "Feed", newWorker.getUrl());
    }
}
