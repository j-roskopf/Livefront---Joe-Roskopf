package com.livefront.di

import android.app.Application
import android.content.Context
import com.livefront.LivefrontApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideApplication(application: LivefrontApplication): Application {
        return application
    }

    @Provides
    internal fun provideContext(application: LivefrontApplication): Context {
        return application.applicationContext
    }
}