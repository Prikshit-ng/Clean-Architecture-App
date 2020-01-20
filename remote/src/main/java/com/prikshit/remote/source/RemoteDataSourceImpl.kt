package com.prikshit.remote.source

import com.prikshit.data.model.DeliveryData
import com.prikshit.data.repository.RemoteDatasource
import com.prikshit.remote.api.DeliveryService
import com.prikshit.remote.mapper.Mapper
import com.prikshit.remote.model.DeliveryNetwork
import io.reactivex.Observable
import java.lang.Exception
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val deliveryMapper: Mapper<DeliveryData, DeliveryNetwork>,
    private val deliveryService: DeliveryService
) : RemoteDatasource {
    override fun getDeliveries(startIndex: String, limit: Int): Observable<List<DeliveryData>> {
        return deliveryService.getDeliveries(startIndex.toInt(), limit)
            .map { response ->
                if(response.code()!=200){
                    throw Exception("Server Error")
                }
                response.body()?.map { delivery: DeliveryNetwork ->
                    deliveryMapper.from(delivery)
                }
            }
    }
}