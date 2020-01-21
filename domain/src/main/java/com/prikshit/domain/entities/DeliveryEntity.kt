package com.prikshit.domain.entities

import java.math.BigDecimal

data class DeliveryEntity(
    val id: String,
    val deliveryFee: String,
    val goodsPicture: String,
    val pickupTime: String,
    val remarks: String,
    val routeEntity: RouteEntity,
    val senderEntity: SenderEntity,
    val surcharge: String,
    var isFav: Boolean
) {
    fun getTotalAmount(): String {
        val dFee = deliveryFee.substring(1).toBigDecimal()
        val dCharge = surcharge.substring(1).toBigDecimal()
        val total = dFee.add(dCharge)

        return deliveryFee.toCharArray()[0].toString() +total.setScale(2, BigDecimal.ROUND_HALF_EVEN)
    }
}