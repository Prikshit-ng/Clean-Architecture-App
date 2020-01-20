package com.prikshit.delivery.di

import com.prikshit.delivery.ui.deliveries.DeliveryListActivity
import com.prikshit.delivery.ui.deliveryDetail.DeliveryDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributesMainActivity(): DeliveryListActivity

    @ContributesAndroidInjector
    internal abstract fun contributesDeliveryDetailActivity(): DeliveryDetailActivity
}