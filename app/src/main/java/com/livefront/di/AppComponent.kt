package com.livefront.di

import com.livefront.LivefrontApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    NetworkModule::class,
    AppModule::class,
    BuildersModule::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: LivefrontApplication): AppComponent.Builder

        fun build(): AppComponent
    }

    fun inject(application: LivefrontApplication)
}