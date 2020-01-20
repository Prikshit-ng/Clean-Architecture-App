package com.prikshit.delivery.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prikshit.delivery.ui.deliveries.viewmodel.DeliveryViewmodel
import com.prikshit.delivery.ui.deliveryDetail.viewmodel.DeliveryDetailViewmodel
import com.prikshit.delivery.ui.factory.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PresentationModule {

    @Binds
    abstract fun bindsViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DeliveryViewmodel::class)
    abstract fun bindsDeliveryViewModel(deliveryVM: DeliveryViewmodel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DeliveryDetailViewmodel::class)
    abstract fun bindsDeliveryDetailViewModel(deliveryDetailVM: DeliveryDetailViewmodel): ViewModel


}