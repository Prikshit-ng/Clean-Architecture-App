package com.prikshit.local.source

import androidx.paging.DataSource
import com.prikshit.data.model.DeliveryData
import com.prikshit.data.repository.LocalDataSource
import com.prikshit.local.database.DeliveryDB
import com.prikshit.local.database.DeliveryDao
import com.prikshit.local.mapper.DeliveryDataMapper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val deliveryDB: DeliveryDB,
    private val deliveryMapper: DeliveryDataMapper,
    private val deliveryDAO: DeliveryDao
) : LocalDataSource {
    override fun clear(): Completable {
        deliveryDB.clearAllTables()
        return deliveryDAO.clear()
    }

    override fun getDeliveryCount(): Single<Int> {
        return deliveryDAO.getDeliveryCount()
    }

    override fun getDeliveryById(deliveryId: String): Observable<DeliveryData> {
        return deliveryDAO.getDeliveryById(deliveryId)
            .map {
                deliveryMapper.from(it)
            }
    }

    override fun getDeliveries(): DataSource.Factory<Int, DeliveryData> {
        return deliveryDAO.getDeliveries().map {
            deliveryMapper.from(it)
        }
    }

    override fun saveDeliveries(deliveries: List<DeliveryData>) {
        deliveryDAO.saveDeliveries(
            deliveries.map {
                deliveryMapper.to(it)
            }
        )
    }

    override fun updateDelivery(delivery: DeliveryData): Completable {
        return deliveryDAO.updateDelivery(
            deliveryMapper.to(delivery)
        )
    }
}