package ru.iandreyshev.parserrss.presentation.presenter.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;

public class RefreshFeedTask extends Task<IFeedInfo, Void, List<IArticleInfo>, RefreshFeedTask.RefreshError> {
    public enum RefreshError {
        BadConnection,
    }

    @Override
    protected List<IArticleInfo> doInBackground(IFeedInfo... feedsToRefresh) {
        List<IArticleInfo> result = new ArrayList<>();

        for (IFeedInfo feed : feedsToRefresh) {
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {}

            if (new Random().nextBoolean()) {
                for (int i = 0; i < 10; ++i) {
                    result.add(new Article(0, "Article name", "Article text"));
                }
            } else {
                cancel(RefreshError.BadConnection);
            }
        }

        return result;
    }
}
