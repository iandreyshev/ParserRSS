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

        private lateinit var _context: WeakReference<Context>
        private lateinit var _boxStore: BoxStore

        val database: Database
            get() = Database(_boxStore)

        fun getStr(id: Int): String {
            return _context.get()?.getString(id) ?: DEFAULT_STRING
        }
    }

    override fun onCreate() {
        super.onCreate()

        _context = WeakReference(this)
        _boxStore = MyObjectBox.builder().androidContext(this).build()
    }
}
