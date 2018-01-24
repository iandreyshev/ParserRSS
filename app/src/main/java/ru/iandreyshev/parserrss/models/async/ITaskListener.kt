package ru.iandreyshev.parserrss.models.async

interface ITaskListener<in T> {
    fun onPreExecute()

    fun onPostExecute(result: T?)
}
