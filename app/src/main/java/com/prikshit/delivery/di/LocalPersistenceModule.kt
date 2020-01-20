package com.prikshit.delivery.di

import android.app.Application
import com.prikshit.data.model.DeliveryData
import com.prikshit.data.repository.LocalDataSource
import com.prikshit.local.database.DeliveryDB
import com.prikshit.local.mapper.DeliveryDataMapper
import com.prikshit.local.mapper.Mapper
import com.prikshit.local.model.DeliveryLocal
import com.prikshit.local.source.LocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [LocalPersistenceModule.Binders::class])
class LocalPersistenceModule {

    @Module
    interface Binders {

        @Binds
        fun bindsLocalDataSource(
            localDataSourceImpl: LocalDataSourceImpl
        ): LocalDataSource

        @Binds
        fun bindDeliveryMapper(
            deliveryDataMapper: DeliveryDataMapper
        ): Mapper<DeliveryData, DeliveryLocal>
    }

    @Provides
    @Singleton
    fun providesDatabase(
        application: Application
    ) = DeliveryDB.getInstance(application.applicationContext)

    @Provides
    @Singleton
    fun providesDeliveryDAO(
        deliveryDB: DeliveryDB
    ) = deliveryDB.getDeliveryDao()

}
