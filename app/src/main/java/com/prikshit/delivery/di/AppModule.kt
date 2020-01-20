package com.prikshit.delivery.di

import android.app.Application
import android.content.Context
import com.prikshit.delivery.utils.NetworkUtil
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    internal fun provideNetworkUtils(context: Context): NetworkUtil {
        return NetworkUtil(context)
    }
}