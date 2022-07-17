package com.xossapp.vulegechi.newReminder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xossapp.vulegechi.network.RetrofitInstance
import com.xossapp.vulegechi.network.UserInterface
import com.xossapp.vulegechi.newReminder.data.ReminderItem
import com.xossapp.vulegechi.newReminder.data.ReminderList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityReminderViewModel:ViewModel() {

    lateinit var deleteReminderData: MutableLiveData<ReminderList>
    lateinit var createReminderData: MutableLiveData<ReminderItem>

    init {
        deleteReminderData = MutableLiveData()
        createReminderData = MutableLiveData()
    }

    fun createReminderListObserver(): MutableLiveData<ReminderItem>
    {
        return createReminderData
    }

    fun deleteReminderListObserver(): MutableLiveData<ReminderList>
    {
        return deleteReminderData
    }

    fun postNewReminder(reminderItem: ReminderItem)
    {
        val retroInstance = RetrofitInstance.getRetrofitInstance().create(UserInterface::class.java)
        val call = retroInstance.set_Remind(reminderItem)
        call.enqueue(object : Callback<ReminderItem> {
            override fun onFailure(call: Call<ReminderItem>, t: Throwable) {
                createReminderData.postValue(null)
            }

            override fun onResponse(call: Call<ReminderItem>, response: Response<ReminderItem>) {
                if (response.isSuccessful) {
                    createReminderData.postValue(response.body())
                } else {
                    createReminderData.postValue(null)
                }
            }
        })
    }

    fun deleteReminder(reminderId:String)
    {
        val retroInstance = RetrofitInstance.getRetrofitInstance().create(UserInterface::class.java)
        val call = retroInstance.deleteReminder(reminderId)

        call.enqueue(object : Callback<ReminderList> {
            override fun onFailure(call: Call<ReminderList>, t: Throwable) {
                deleteReminderData.postValue(null)
            }

            override fun onResponse(call: Call<ReminderList>, response: Response<ReminderList>) {
                if (response.isSuccessful) {
                    deleteReminderData.postValue(response.body())
                } else {
                    deleteReminderData.postValue(null)
                }
            }
        })
    }
}