package ru.iandreyshev.parserrss.app.parserRss;

import android.support.annotation.NonNull;
import android.util.Log;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.Feed;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;

public final class ParserRss implements IParserRss {
    private static final String TAG = "ParserRss";
    private static final String NOT_PARSED_EXCEPTION_MESSAGE = "The parser did not execute the parsing successfully";
    private static final String DISABLE_DTD_FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

    private IParserRss mSuccessParser;
    private ParserRssResult mResult = ParserRssResult.NotParse;
    private List<SAXBuilder> mBuilders = new ArrayList<>();
    private List<IParserRss> mParsers = new ArrayList<>();

    public ParserRss() {
        initBuilders();
        initParsers();
    }

    public void parse(@NonNull final String rssText) {
        final Document document = toDocument(rssText);

        if (document == null) {
            mResult = ParserRssResult.InvalidXmlFormat;

            return;
        }

        parse(document);
    }

    @Override
    public void parse(@NonNull final Document rss) {
        parse(rss, true);
    }

    @Override
    public void parseFeed(@NonNull final Document rss) {
        parse(rss, false);
    }

    public ParserRssResult getResult() {
        return mResult;
    }

    @Override
    public List<Article> getArticles() throws IllegalStateException {
        if (getResult() != ParserRssResult.Success) {
            throw new IllegalStateException(NOT_PARSED_EXCEPTION_MESSAGE);
        }

        return mSuccessParser.getArticles();
    }

    @Override
    public Feed getFeed() throws IllegalStateException {
        if (getResult() != ParserRssResult.Success) {
            throw new IllegalStateException(NOT_PARSED_EXCEPTION_MESSAGE);
        }

        return mSuccessParser.getFeed();
    }

    private void initBuilders() {
        mBuilders.add(new SAXBuilder());

        final SAXBuilder withoutDtdBuilder = new SAXBuilder();
        withoutDtdBuilder.setFeature(DISABLE_DTD_FEATURE, false);
    }

    private void initParsers() {
        mParsers.add(new Parser_0_91());
        mParsers.add(new Parser_1_0());
        mParsers.add(new Parser_2_0());
    }

    private void parse(@NonNull final Document rss, boolean isFully) {
        for (final IParserRss parser : mParsers) {

            if (isFully) {
                parser.parse(rss);
            } else {
                parser.parseFeed(rss);
            }

            mResult = parser.getResult();

            if (mResult == ParserRssResult.Success) {
                mSuccessParser = parser;

                break;
            }
        }
    }

    private Document toDocument(@NonNull final String xmlText) {
        Document result = null;

        for (final SAXBuilder builder : mBuilders) {
            try {
                result = builder.build(new StringReader(xmlText));

                if (result != null) {
                    break;
                }
            } catch (Exception ex) {
            }
        }

        return result;
    }
}
