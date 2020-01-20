package com.prikshit.remote.model

import com.google.gson.annotations.SerializedName

data class DeliveryNetwork(
    @SerializedName("id")val id: String,
    @SerializedName("deliveryFee")val deliveryFee: String,
    @SerializedName("goodsPicture")val goodsPicture: String,
    @SerializedName("pickupTime")val pickupTime: String,
    @SerializedName("remarks")val remarks: String,
    @SerializedName("route")val routeNetwork: RouteNetwork,
    @SerializedName("sender")val senderNetwork: SenderNetwork,
    @SerializedName("surcharge")val surcharge: String
)