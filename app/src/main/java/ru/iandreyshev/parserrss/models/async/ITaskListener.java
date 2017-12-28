package ru.iandreyshev.parserrss.models.async;

interface ITaskListener<T> {
    void onPreExecute();

    void onPostExecute(T result);
}
