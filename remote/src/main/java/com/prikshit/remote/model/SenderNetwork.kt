package com.prikshit.remote.model

import com.google.gson.annotations.SerializedName

data class SenderNetwork(
    @SerializedName("email") val email: String,
    @SerializedName("name")val name: String,
    @SerializedName("phone")val phone: String
)