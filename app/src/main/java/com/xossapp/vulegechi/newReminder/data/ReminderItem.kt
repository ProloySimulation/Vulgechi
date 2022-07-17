package com.xossapp.vulegechi.newReminder.data

import com.google.gson.annotations.SerializedName

data class ReminderList(val set_remind: ArrayList<ReminderItem>)

data class ReminderItem(
    @SerializedName("id")
    val reminderId:String?,
    @SerializedName("address")
    val mobile:String?,
    @SerializedName("first_layer")
    val ocassionName:String?,
    @SerializedName("second_layer")
    val personType:String?,
    @SerializedName("third_layer")
    val ocassionDate:String?,
    @SerializedName("fourth_layer")
    val ocassionPerson:String?,
    @SerializedName("fifth_layer")
    val personNumber:String?,
    @SerializedName("occasion_time")
    val ocassionTime:String?,
    @SerializedName("message")
    val message:String?,
)
