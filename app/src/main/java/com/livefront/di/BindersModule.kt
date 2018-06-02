package com.livefront.di

import com.livefront.detail.ui.DetailActivity
import com.livefront.main.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun bindsDetailActivity(): DetailActivity
}