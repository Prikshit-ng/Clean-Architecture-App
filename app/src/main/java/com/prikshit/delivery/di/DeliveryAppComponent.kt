package com.prikshit.delivery.di

import android.app.Application
import com.prikshit.delivery.application.DeliveryApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        DomainModule::class,
        DataModule::class,
        LocalPersistenceModule::class,
        RemoteModule::class,
        PresentationModule::class,
        AppModule::class
    ]
)
interface DeliveryAppComponent : AndroidInjector<DeliveryApplication> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): DeliveryAppComponent
    }

    override fun inject(app: DeliveryApplication)
}