package ru.iandreyshev.parserrss.app.parserRss;

import android.support.annotation.NonNull;

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
    private static final List<IParserRss> PARSERS = new ArrayList<>();
    private static final String NOT_PARSED_EXCEPTION_MESSAGE = "The parser did not execute the parsing successfully";
    private static final String DISABLE_DTD_FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

    static {
        PARSERS.add(new Parser_0_91());
        PARSERS.add(new Parser_1_0());
        PARSERS.add(new Parser_2_0());
    }

    private IParserRss mSuccessParser;
    private ParserRssResult mResult = ParserRssResult.NotParse;

    public ParserRssResult parse(@NonNull final String rssText) {
        try {
            final SAXBuilder builder = new SAXBuilder();
            builder.setFeature(DISABLE_DTD_FEATURE, false);

            final Document document = builder.build(new StringReader(rssText));
            mResult = parse(document);

        } catch (Exception ex) {
            mResult = ParserRssResult.InvalidXmlFormat;
        }

        return getResult();
    }

    @Override
    public ParserRssResult parse(@NonNull final Document xml) {
        for (final IParserRss parser : PARSERS) {
            if (parser.parse(xml) == ParserRssResult.Success) {
                mSuccessParser = parser;
                mResult = ParserRssResult.Success;

                break;
            }

            mResult = ParserRssResult.InvalidRssFormat;
        }

        return getResult();
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
}
