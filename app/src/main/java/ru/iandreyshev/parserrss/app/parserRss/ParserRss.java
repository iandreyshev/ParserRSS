package ru.iandreyshev.parserrss.app.parserRss;

import android.support.annotation.NonNull;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;

public final class ParserRss implements IParserRss {
    private static final List<IParserRss> PARSERS = new ArrayList<>();
    private static final String NOT_PARSED_EXCEPTION_MESSAGE = "The parser did not execute the parsing successfully";

    static {
        PARSERS.add(new Parser_0_91());
        PARSERS.add(new Parser_1_0());
        PARSERS.add(new Parser_2_0());
    }

    private IParserRss mSuccessParser;
    private ParserRssResult mResult = ParserRssResult.NotParse;

    public ParserRssResult parse(@NonNull final String rssText) {
        Document xmlDoc;

        try {
            xmlDoc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(rssText);
            mResult = parse(xmlDoc);
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

            mResult = ParserRssResult.InvalidRssFromat;
        }

        return getResult();
    }

    public ParserRssResult getResult() {
        return mResult;
    }

    @Override
    public List<IArticleInfo> getArticles() throws IllegalStateException {
        if (getResult() != ParserRssResult.Success) {
            throw new IllegalStateException(NOT_PARSED_EXCEPTION_MESSAGE);
        }

        return mSuccessParser.getArticles();
    }

    @Override
    public IFeedInfo getFeed() throws IllegalStateException {
        if (getResult() != ParserRssResult.Success) {
            throw new IllegalStateException(NOT_PARSED_EXCEPTION_MESSAGE);
        }

        return mSuccessParser.getFeed();
    }
}
