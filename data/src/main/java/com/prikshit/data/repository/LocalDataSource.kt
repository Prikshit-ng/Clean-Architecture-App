package com.prikshit.data.repository

import androidx.paging.DataSource
import com.prikshit.data.model.DeliveryData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface LocalDataSource {

    fun getDeliveryById(deliveryId: String): Observable<DeliveryData>
    fun getDeliveries(): DataSource.Factory<Int, DeliveryData>
    fun saveDeliveries(deliveries: List<DeliveryData>)
    fun updateDelivery(delivery: DeliveryData): Completable
    fun getDeliveryCount(): Single<Int>
    fun clear(): Completable
}