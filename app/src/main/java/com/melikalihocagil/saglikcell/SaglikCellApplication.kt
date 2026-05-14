package com.melikalihocagil.saglikcell

import android.app.Application
import com.melikalihocagil.saglikcell.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Uygulamanın giriş noktası. Koin konfigürasyonu burada yapılır.
 */
class SaglikCellApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@SaglikCellApplication)
            modules(appModule)
        }
    }
}
