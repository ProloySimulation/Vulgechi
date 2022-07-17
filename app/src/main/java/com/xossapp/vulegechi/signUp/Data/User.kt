package com.xossapp.vulegechi.signUp.Data

import com.google.gson.annotations.SerializedName

data class user(
    @SerializedName("address")
    val mobile:String,
    @SerializedName("status")
    val status:String?,
    @SerializedName("message")
    val response:String?,
    @SerializedName("token")
    val token:String?
)
