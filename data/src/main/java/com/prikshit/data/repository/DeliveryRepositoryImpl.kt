package com.prikshit.data.repository

import androidx.paging.DataSource
import com.prikshit.data.mapper.Mapper
import com.prikshit.data.model.DeliveryData
import com.prikshit.domain.entities.DeliveryEntity
import com.prikshit.domain.repository.DeliveryRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class DeliveryRepositoryImpl @Inject constructor(
    private val deliveryMapper: Mapper<DeliveryEntity, DeliveryData>,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDatasource
) : DeliveryRepository {

    override fun getDeliveryInfo(deliveryId: String): Observable<DeliveryEntity> {
        return localDataSource.getDeliveryById(deliveryId)
            .map { deliveryMapper.from(it) }
            .onErrorResumeNext(Observable.empty())
    }

    override fun getDeliveryList(startIndex: String, limit: Int): Observable<List<DeliveryEntity>> {

        return remoteDataSource.getDeliveries(startIndex, limit)
            .map { deliveries ->
                if (startIndex.toInt() == 0) localDataSource.clear()
                localDataSource.saveDeliveries(deliveries)
                deliveries.map { deliveryMapper.from(it) }
            }
    }

    override fun getDeliveryList(limit: Int): DataSource.Factory<Int, DeliveryEntity> {
        return localDataSource.getDeliveries().map { deliveryMapper.from(it) }
    }

    override fun getDeliveryCount(): Single<Int> {
        return localDataSource.getDeliveryCount()
    }

    override fun updateDelivery(delivery: DeliveryEntity): Completable {
        return localDataSource.updateDelivery(
            deliveryMapper.to(delivery)
        )
    }

    override fun clearCache(): Completable {
        return localDataSource.clear()
    }
}