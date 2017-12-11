package ru.iandreyshev.parserrss.app.parserRss;

import android.support.annotation.NonNull;

import org.w3c.dom.Document;

import java.util.List;

import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;

final class Parser_1_0 implements IParserRss {
    @Override
    public ParserRssResult parse(@NonNull Document xml) {
        return getResult();
    }

    @Override
    public ParserRssResult getResult() {
        return null;
    }

    @Override
    public List<IArticleInfo> getArticles() {
        return null;
    }

    @Override
    public IFeedInfo getFeed() {
        return null;
    }
}
