package ru.iandreyshev.parserrss.app;

import org.junit.Test;

import static org.junit.Assert.*;
import static ru.iandreyshev.parserrss.app.Utils.toTabTitle;

public class UtilsTest {
    private static final String ONE_WORD_TITLE = "TITLE";
    private static final String NON_TRIMMED_STRING = "   " + ONE_WORD_TITLE + "  \n  ";

    @Test
    public void truncateTrimString() {
        final String result = toTabTitle(NON_TRIMMED_STRING);

        assertEquals(result, ONE_WORD_TITLE);
    }

    @Test
    public void toBitmapReturnNullFromNullArray() {
        assertNull(Utils.toBitmap(null));
    }

    @Test
    public void toBitmapReturnNullFromEmptyArray() {
        final byte[] array = {};

        assertNull(Utils.toBitmap(array));
    }
}