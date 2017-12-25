package ru.iandreyshev.parserrss.models.async;

public interface ITaskListener<T> {
    void onPreExecute();

    void onPostExecute(T result);
}
