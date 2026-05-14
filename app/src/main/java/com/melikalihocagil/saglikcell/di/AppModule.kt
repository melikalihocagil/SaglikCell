package com.melikalihocagil.saglikcell.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.melikalihocagil.saglikcell.data.manager.TokenManagerImpl
import com.melikalihocagil.saglikcell.domain.manager.TokenManager
import com.melikalihocagil.saglikcell.presentetion.SaglikCellAppViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val Context.dataStore by preferencesDataStore(name = "settings")

/**
 * Uygulama genelindeki bağımlılıkları (DI) tanımlayan Koin modülü.
 */
val appModule = module {
    // DataStore & TokenManager
    single<TokenManager> { TokenManagerImpl(androidContext().dataStore) }

    // ViewModels
    viewModelOf(::SaglikCellAppViewModel)
}
