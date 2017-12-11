package ru.iandreyshev.parserrss.app.parserRss;

import android.support.annotation.NonNull;

import org.w3c.dom.Document;

import java.util.List;

import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;

interface IParserRss {
    ParserRssResult parse(@NonNull final Document xml);

    ParserRssResult getResult();

    List<IArticleInfo> getArticles();

    IFeedInfo getFeed();
}
