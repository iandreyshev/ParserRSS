package ru.iandreyshev.parserrss.app

import android.app.Application
import android.content.Context

import io.objectbox.BoxStore
import ru.iandreyshev.parserrss.models.repository.Database
import ru.iandreyshev.parserrss.models.repository.MyObjectBox

class App : Application() {
    companion object {
        private lateinit var mCtxInstance: Context
        private lateinit var mBoxStore: BoxStore

        val database: Database
            get() = Database(mBoxStore)

        fun getStr(id: Int): String = mCtxInstance.getString(id)
    }

    override fun onCreate() {
        super.onCreate()
        mCtxInstance = this
        mBoxStore = MyObjectBox.builder().androidContext(this).build()
    }
}
