package com.xossapp.vulegechi.wish.data

import com.google.gson.annotations.SerializedName

data class WishList(

    val set_remind: ArrayList<Wish>)

data class Wish (
    @SerializedName("address")
    val address:String,
    @SerializedName("date")
    val wishDate:String,
    @SerializedName("message")
    val wishMessage:String,
    @SerializedName("receiver")
    val wishReciever:String
    )