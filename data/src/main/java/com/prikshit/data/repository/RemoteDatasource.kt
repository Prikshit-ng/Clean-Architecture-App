package com.prikshit.data.repository

import com.prikshit.data.model.DeliveryData
import io.reactivex.Observable

interface RemoteDatasource {

    fun getDeliveries(startIndex: String, limit: Int): Observable<List<DeliveryData>>
}