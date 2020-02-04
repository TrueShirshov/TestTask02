package org.shirshov.testtask02

import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.shirshov.testtask02.util.LogUtil
import org.shirshov.testtask02.util.extension.Ui
import org.shirshov.testtask02.util.testTask02Module

@Suppress("unused") // Used from manifest
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initGlobalExceptionHandler()
        Ui.baseContext = baseContext
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(testTask02Module)
        }
    }

    private fun initGlobalExceptionHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            LogUtil.e("UncaughtException handler:", throwable)
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}