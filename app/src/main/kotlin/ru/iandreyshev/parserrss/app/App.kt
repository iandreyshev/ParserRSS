package ru.iandreyshev.parserrss.app

import android.app.Application
import android.content.Context

import io.objectbox.BoxStore
import ru.iandreyshev.parserrss.models.repository.*
import java.lang.ref.WeakReference

class App : Application() {
    companion object {
        private const val DEFAULT_STRING = "?"

        private lateinit var mCtxInstance: WeakReference<Context>
        private lateinit var mBoxStore: BoxStore

        val repository: IRepository
            get() = ObjectBoxRepository(mBoxStore)

        fun getStr(id: Int): String = mCtxInstance.get()?.getString(id) ?: DEFAULT_STRING
    }

    override fun onCreate() {
        super.onCreate()
        mCtxInstance = WeakReference(this)
        mBoxStore = MyObjectBox.builder().androidContext(this).build()
    }
}
