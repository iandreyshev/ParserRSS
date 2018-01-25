package ru.iandreyshev.parserrss.models.async

interface ITaskListener<T, U, V> {
    fun onPreExecute() {}

    fun onProgressUpdate(vararg progress: U) {}

    fun onPostExecute(result: V?) {}
}
