package com.prikshit.delivery.di

import com.prikshit.data.mapper.DeliveryDomainMapper
import com.prikshit.data.mapper.Mapper
import com.prikshit.data.model.DeliveryData
import com.prikshit.data.repository.DeliveryRepositoryImpl
import com.prikshit.domain.entities.DeliveryEntity
import com.prikshit.domain.repository.DeliveryRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {

    @Binds
    abstract fun bindsRepository(
        repoImpl: DeliveryRepositoryImpl
    ): DeliveryRepository

    @Binds
    abstract fun bindsDeliveryMapper(
        mapper: DeliveryDomainMapper
    ): Mapper<DeliveryEntity, DeliveryData>
}