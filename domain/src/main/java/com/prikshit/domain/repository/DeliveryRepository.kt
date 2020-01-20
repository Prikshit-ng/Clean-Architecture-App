package com.prikshit.domain.repository

import androidx.paging.DataSource
import com.prikshit.domain.entities.DeliveryEntity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface DeliveryRepository {

    fun getDeliveryInfo(deliveryId: String): Observable<DeliveryEntity>

    fun getDeliveryList(startIndex: String, limit: Int): Observable<List<DeliveryEntity>>

    fun getDeliveryList(limit: Int): DataSource.Factory<Int, DeliveryEntity>

    fun getDeliveryCount(): Single<Int>

    fun clearCache(): Completable

    fun updateDelivery(delivery: DeliveryEntity): Completable
}