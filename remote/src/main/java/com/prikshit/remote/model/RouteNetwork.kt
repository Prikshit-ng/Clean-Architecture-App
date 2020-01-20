package com.prikshit.remote.model

import com.google.gson.annotations.SerializedName

data class RouteNetwork(
    @SerializedName("end")val end: String,
    @SerializedName("start")val start: String
)