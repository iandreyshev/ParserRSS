package ru.iandreyshev.parserrss.models.filters;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ru.iandreyshev.parserrss.models.repository.Article;

public class FilterByDate implements IArticlesFilter {
    private static final int EQUALS = 0;
    private static final int LESS = -1;
    private static final int GREATER = 1;

    @NonNull
    public static FilterByDate newInstance() {
        return new FilterByDate();
    }

    @Override
    @NonNull
    public List<Article> sort(@NonNull final List<Article> articles) {
        final Set<Article> sortedSet = new TreeSet<>((final Article right, final Article left) -> {
            if (right.getDate() == null && left.getDate() == null) {
                return EQUALS;
            } else if (right.getDate() == null) {
                return LESS;
            } else if (left.getDate() == null) {
                return GREATER;
            }

            return left.getDate().compareTo(right.getDate());
        });

        sortedSet.addAll(articles);

        return new ArrayList<>(sortedSet);
    }
}
