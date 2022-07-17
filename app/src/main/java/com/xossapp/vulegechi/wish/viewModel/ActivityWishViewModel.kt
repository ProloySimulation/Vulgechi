package com.xossapp.vulegechi.wish.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xossapp.vulegechi.home.ReminderAdapter
import com.xossapp.vulegechi.network.RetrofitInstance
import com.xossapp.vulegechi.network.UserInterface
import com.xossapp.vulegechi.newReminder.data.ReminderItem
import com.xossapp.vulegechi.newReminder.data.ReminderList
import com.xossapp.vulegechi.wish.adapter.WishAdapter
import com.xossapp.vulegechi.wish.data.Wish
import com.xossapp.vulegechi.wish.data.WishList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityWishViewModel:ViewModel() {

    lateinit var wishList: MutableLiveData<WishList>
    lateinit var wishData: MutableLiveData<Wish>
    lateinit var wishAdapter: WishAdapter

    init {
        wishList = MutableLiveData()
        wishData = MutableLiveData()
        wishAdapter = WishAdapter()
    }

    fun createWishObserver(): MutableLiveData<Wish>
    {
        return wishData
    }

    fun getAdapter(): WishAdapter
    {
        return wishAdapter
    }

    fun setAdapter(data:ArrayList<Wish>)
    {
        wishAdapter.setDataList(data)
        wishAdapter.notifyDataSetChanged()
    }

    fun getWishListObserver(): MutableLiveData<WishList>
    {
        return wishList
    }

    fun setWish(wish: Wish)
    {
        val retroInstance = RetrofitInstance.getRetrofitInstance().create(UserInterface::class.java)
        val call = retroInstance.set_Wish(wish)
        call.enqueue(object : Callback<Wish> {

            override fun onFailure(call: Call<Wish>, t: Throwable) {
                wishData.postValue(null)
            }

            override fun onResponse(call: Call<Wish>, response: Response<Wish>) {
                if (response.isSuccessful) {
                    wishData.postValue(response.body())
                } else {
                    wishData.postValue(null)
                }
            }
        })
    }

    fun getWishList(mobile: String)
    {
        val retroInstance = RetrofitInstance.getRetrofitInstance().create(UserInterface::class.java)
        val call = retroInstance.getWish(mobile)

        call.enqueue(object : Callback<WishList> {
            override fun onFailure(call: Call<WishList>, t: Throwable) {
                wishList.postValue(null)
            }

            override fun onResponse(call: Call<WishList>, response: Response<WishList>) {
                if (response.isSuccessful) {
                    wishList.postValue(response.body())
                } else {
                    wishList.postValue(null)
                }
            }
        })
    }
}