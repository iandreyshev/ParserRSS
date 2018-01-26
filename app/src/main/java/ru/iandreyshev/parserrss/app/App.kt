package ru.iandreyshev.parserrss.app

import android.app.Application
import android.content.Context

import java.lang.ref.WeakReference

import io.objectbox.BoxStore
import ru.iandreyshev.parserrss.models.repository.Database
import ru.iandreyshev.parserrss.models.repository.MyObjectBox

class App : Application() {
    companion object {
        private const val DEFAULT_STRING = ""

        private lateinit var context: WeakReference<Context>
        private lateinit var boxStore: BoxStore

        val database: Database
            get() = Database(boxStore)

        fun getStr(id: Int): String {
            return context.get()?.getString(id) ?: DEFAULT_STRING
        }
    }

    override fun onCreate() {
        super.onCreate()

        context = WeakReference(this)
        boxStore = MyObjectBox.builder().androidContext(this).build()
    }
}
