package ru.iandreyshev.parserrss.models.rss;

import java.util.ArrayList;
import java.util.List;

public class RssParser {
    private static final List<RssParseEngine> mParsers = new ArrayList<>();

    static {
        mParsers.add(new RssParserV2());
    }

    public static Rss parse(final String rssText) {
        for (final RssParseEngine parser : mParsers) {
            final Rss rss = parser.parse(rssText);

            if (rss != null) {
                return rss;
            }
        }

        return null;
    }
}
