package ru.iandreyshev.parserrss.app

import android.app.Application
import android.content.Context

import io.objectbox.BoxStore
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.repository.MyObjectBox
import ru.iandreyshev.parserrss.models.repository.Repository
import java.lang.ref.WeakReference

class App : Application() {
    companion object {
        private lateinit var mCtxInstance: WeakReference<Context>
        private lateinit var mBoxStore: BoxStore

        val repository: IRepository
            get() = Repository(mBoxStore)

        fun getStr(id: Int): String = mCtxInstance.get()?.getString(id) ?: ""
    }

    override fun onCreate() {
        super.onCreate()
        mCtxInstance = WeakReference(this)
        mBoxStore = MyObjectBox.builder().androidContext(this).build()
    }
}
