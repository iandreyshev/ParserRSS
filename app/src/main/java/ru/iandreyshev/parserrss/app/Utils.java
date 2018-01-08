package ru.iandreyshev.parserrss.app;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ru.iandreyshev.parserrss.models.repository.Article;
import ru.iandreyshev.parserrss.models.repository.Rss;

public class Utils {
    private static final String CUT_TAB_TITLE_PATTERN = "%s...";
    private static final int MAX_TAB_TITLE_LINE_LENGTH = 16;

    @NonNull
    public static String truncateTabsTitle(@NonNull String title) {
        title = title.trim();
        int firstSpacePos = title.indexOf(" ");
        int maxLength = MAX_TAB_TITLE_LINE_LENGTH;

        if (firstSpacePos > 0 && firstSpacePos < MAX_TAB_TITLE_LINE_LENGTH) {
            title = title.replaceFirst(" ", "\n");
            maxLength += firstSpacePos;
        }

        if (title.length() > maxLength) {
            title = title.substring(0, maxLength - 1);
            title = String.format(CUT_TAB_TITLE_PATTERN, title);
        }

        return title;
    }

    @NonNull
    public static List<Article> sortByDateDESC(@NonNull final List<Article> list) {
        Set<Article> sortedSet = new TreeSet<>((final Article right, final Article left) -> {
            if (right.getPostDate() == null && left.getPostDate() == null) {
                return 0;
            } else if (right.getPostDate() == null) {
                return -1;
            } else if (left.getPostDate() == null) {
                return 1;
            }

            return left.getPostDate().compareTo(right.getPostDate());
        });

        sortedSet.addAll(list);

        return new ArrayList<>(sortedSet);
    }
}
