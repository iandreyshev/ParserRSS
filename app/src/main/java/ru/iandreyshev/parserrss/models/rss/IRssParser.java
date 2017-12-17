package ru.iandreyshev.parserrss.models.rss;

interface IRssParser {
    Rss parse(final String rss);
}
