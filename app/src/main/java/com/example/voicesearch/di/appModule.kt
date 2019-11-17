package com.example.voicesearch.di

import com.example.voicesearch.VoiceSearchApplication
import com.example.voicesearch.viewModel.RecentSearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { RecentSearchViewModel(get()) }
    single {
        androidContext() as VoiceSearchApplication
    }
}