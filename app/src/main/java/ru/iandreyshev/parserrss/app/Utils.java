package ru.iandreyshev.parserrss.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Locale;

public final class Utils {
    private static final int MAX_BYTES_COUNT = 1048576; // 1MB
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

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

    @Nullable
    public static Bitmap toBitmap(@Nullable final byte[] bytes) {
        if (bytes == null || bytes.length == 0 || bytes.length > MAX_BYTES_COUNT) {
            return null;
        }

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @NonNull
    public static String toDateStr(@Nullable Long date) {
        return date == null ? "" : DATE_FORMAT.format(date);
    }
}