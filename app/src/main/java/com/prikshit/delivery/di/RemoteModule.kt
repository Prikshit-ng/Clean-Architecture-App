package com.prikshit.delivery.di

import com.prikshit.data.model.DeliveryData
import com.prikshit.data.repository.RemoteDatasource
import com.prikshit.delivery.BuildConfig
import com.prikshit.remote.api.DeliveryService
import com.prikshit.remote.mapper.DeliveryNetworkMapper
import com.prikshit.remote.mapper.Mapper
import com.prikshit.remote.model.DeliveryNetwork
import com.prikshit.remote.source.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module(includes = [RemoteModule.Binders::class])
class RemoteModule {

    @Module
    interface Binders {

        @Binds
        fun bindsRemoteSource(
            remoteDataSourceImpl: RemoteDataSourceImpl
        ): RemoteDatasource

        @Binds
        fun bindDeliveryMapper(
            transactionMapper: DeliveryNetworkMapper
        ): Mapper<DeliveryData, DeliveryNetwork>
    }

    @Provides
    fun providesDeliveryService(retrofit: Retrofit): DeliveryService =
        retrofit.create(DeliveryService::class.java)


    @Provides
    fun providesRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.SERVER_URL)
            .build()
    }
}