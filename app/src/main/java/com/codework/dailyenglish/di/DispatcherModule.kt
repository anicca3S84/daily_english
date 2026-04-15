package com.codework.dailyenglish.di

import com.codework.dailyenglish.core.dispatchers.Dispatcher
import com.codework.dailyenglish.core.dispatchers.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherModule {
    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(
        impl: DispatcherProvider
    ): Dispatcher
}