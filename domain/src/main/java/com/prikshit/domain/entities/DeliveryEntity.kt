package com.prikshit.domain.entities

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
        val dFee = deliveryFee.substring(1).toDouble()
        val dCharge = surcharge.substring(1).toDouble()
        val total = (dFee + dCharge)

        return deliveryFee.toCharArray()[0] + "%.2f".format(total)
    }
}