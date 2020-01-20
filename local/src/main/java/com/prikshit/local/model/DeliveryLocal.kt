package com.prikshit.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "delivery")
data class DeliveryLocal(
    @PrimaryKey val id: String,
    val deliveryFee: String,
    val goodsPicture: String,
    val pickupTime: String,
    val remarks: String,
    val routeEnd: String,
    val routeStart: String,
    val senderEmail: String,
    val senderName: String,
    val senderPhone: String,
    val surcharge: String,
    var isFav: Boolean
)