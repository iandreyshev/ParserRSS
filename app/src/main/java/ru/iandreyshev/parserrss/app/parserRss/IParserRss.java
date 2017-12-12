package ru.iandreyshev.parserrss.app.parserRss;

import android.support.annotation.NonNull;

import org.jdom2.Document;

import java.util.List;

import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.feed.Feed;

interface IParserRss {
    ParserRssResult parse(@NonNull final Document xml);

    ParserRssResult getResult();

    List<Article> getArticles();

    Feed getFeed();
}
