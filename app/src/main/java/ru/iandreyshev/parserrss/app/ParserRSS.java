package ru.iandreyshev.parserrss.app;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.models.article.IArticleInfo;

public final class ParserRSS {
    private List<IArticleInfo> mArticles = new ArrayList<>();
    private boolean mIsSuccess;

    public void parse(final String rss) {
    }

    public List<IArticleInfo> getArticles() {
        return mArticles;
    }

    public boolean isSuccess() {
        return mIsSuccess;
    }
}
