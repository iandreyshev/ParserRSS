package ru.iandreyshev.parserrss.models.rss;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.models.repository.Rss;

public class RssParser {
    private static final List<RssParseEngine> mParsers = new ArrayList<>();

    static {
        mParsers.add(new RssParserV2());
    }

    @Nullable
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
