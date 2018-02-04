package ru.iandreyshev.parserrss.models.async

interface ITaskListener<T, in U, in V> {
    fun onPreExecute() {}

    fun onProgressUpdate(vararg progress: U) {}

    fun onPostExecute(result: V?) {}
}
