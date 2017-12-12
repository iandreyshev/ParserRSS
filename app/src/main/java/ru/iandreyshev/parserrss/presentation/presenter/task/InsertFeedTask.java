package ru.iandreyshev.parserrss.presentation.presenter.task;

import android.util.Log;

import java.io.Console;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import ru.iandreyshev.parserrss.app.NetWorker;
import ru.iandreyshev.parserrss.app.parserRss.ParserRss;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;

public class InsertFeedTask extends Task<String, Void, IFeedInfo, InsertFeedTask.Status> {
    private static final String TAG = "InsertTask";

    public enum Status {
        InternalError,
        BadConnection,
        InvalidUrl,
        InvalidXmlFormat,
        InvalidRssFormat,
    }

    @Override
    protected IFeedInfo doInBackground(String... urlCollection) {
        if (urlCollection.length < 0) {
            setError(Status.InternalError);
        }

        final String url = urlCollection[0];
        Log.d(TAG, String.format("Url is '%s'", url));
        System.out.println();
        final NetWorker newWorker = new NetWorker();
        newWorker.sendGet(url);
        Log.d(TAG, String.format("Request on '%s' send", url));
        Log.d(TAG, String.format("Net worker status is %s", newWorker.getStatus()));

        switch (newWorker.getStatus()) {

            case BadUrl:
                setError(Status.BadConnection);
                return null;

            case BadConnection:
                setError(Status.InvalidUrl);
                return null;
        }

        final ParserRss parser = new ParserRss();
        parser.parse(newWorker.getResponseAsText());

        Log.d(TAG, String.format("Parser result is %s", parser.getResult()));
        switch (parser.getResult()) {

            case InvalidRssFormat:
                setError(Status.InvalidRssFormat);
                return null;

            case InvalidXmlFormat:
                setError(Status.InvalidXmlFormat);
                return null;
        }

        return parser.getFeed();
    }
}
