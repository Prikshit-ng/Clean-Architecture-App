package com.prikshit.remote.api

import com.prikshit.remote.model.DeliveryNetwork
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DeliveryService {

    @GET("/v2/deliveries")
    fun getDeliveries(@Query("offset") offset: Int, @Query("limit") limit: Int):
            Observable<Response<List<DeliveryNetwork>>>
}