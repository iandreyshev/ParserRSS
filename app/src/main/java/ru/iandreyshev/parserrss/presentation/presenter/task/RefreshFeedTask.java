package ru.iandreyshev.parserrss.presentation.presenter.task;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;

public class RefreshFeedTask
        extends Task<IFeedInfo, Void, List<IArticleInfo>, RefreshFeedTask.Status> {
    public enum Status {
        BadConnection,
        InvalidUrl,
        InvalidXmlFormat,
    }

    @Override
    protected List<IArticleInfo> doInBackground(IFeedInfo... feedsToRefresh) {
        List<IArticleInfo> result = new ArrayList<>();

        if (feedsToRefresh.length < 1) {
            return result;
        }

        return result;
    }
}
