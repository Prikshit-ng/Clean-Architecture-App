package com.prikshit.data.model


data class DeliveryData(
    val id: String,
    val deliveryFee: String,
    val goodsPicture: String,
    val pickupTime: String,
    val remarks: String,
    val routeData: RouteData,
    val senderData: SenderData,
    val surcharge: String,
    val isFav: Boolean
)