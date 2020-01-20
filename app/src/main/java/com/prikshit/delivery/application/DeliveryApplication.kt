package com.prikshit.delivery.application

import com.prikshit.delivery.di.DaggerDeliveryAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class DeliveryApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerDeliveryAppComponent.builder().application(this).build()
    }
}