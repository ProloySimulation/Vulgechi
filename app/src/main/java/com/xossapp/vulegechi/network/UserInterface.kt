package com.xossapp.vulegechi.network

import com.xossapp.vulegechi.newReminder.data.ReminderItem
import com.xossapp.vulegechi.newReminder.data.ReminderList
import com.xossapp.vulegechi.signUp.Data.user
import com.xossapp.vulegechi.wish.data.Wish
import com.xossapp.vulegechi.wish.data.WishList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserInterface {

    @POST("Vlegc/public/api/login")
    suspend fun login(@Body login: user): Response<user>

    @POST("Vlegc/public/api/set_remind")
    fun set_Remind(@Body remind: ReminderItem): Call<ReminderItem>

    @POST("Vlegc/public/api/set_wish")
    fun set_Wish(@Body wish: Wish): Call<Wish>

    @GET("Vlegc/public/api/get_remind")
    fun getReminder(@Query("address")query : String):Call<ReminderList>

    @GET("Vlegc/public/api/get_wish")
    fun getWish(@Query("address")query : String):Call<WishList>

    @DELETE("Vlegc/public/api/delete_remind")
    fun deleteReminder(@Query("id")query : String):Call<ReminderList>

    @DELETE("Vlegc/public/api/delete_wish")
    fun deleteWish(@Query("id")query : String):Call<WishList>

    @FormUrlEncoded
    @POST("Vulegechi/check_subscription.php")
    suspend fun check_sub(@Field ("user_mobile")mobile:String): Response<user>

    @FormUrlEncoded
    @POST("Vulegechi/subscription.php")
    suspend fun subscription(@Field ("user_mobile")mobile:String): Response<user>
}