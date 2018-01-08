package ru.iandreyshev.parserrss.app;

import android.support.annotation.NonNull;

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
}
