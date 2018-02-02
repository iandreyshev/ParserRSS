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

        private lateinit var CONTEXT: WeakReference<Context>
        private lateinit var BOX_STORE: BoxStore

        val database: Database
            get() = Database(BOX_STORE)

        fun getStr(id: Int): String {
            return CONTEXT.get()?.getString(id) ?: DEFAULT_STRING
        }
    }

    override fun onCreate() {
        super.onCreate()

        CONTEXT = WeakReference(this)
        BOX_STORE = MyObjectBox.builder().androidContext(this).build()
    }
}
