package com.xossapp.vulegechi.home


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xossapp.vulegechi.network.RetrofitInstance
import com.xossapp.vulegechi.network.UserInterface
import com.xossapp.vulegechi.newReminder.data.ReminderItem
import com.xossapp.vulegechi.newReminder.data.ReminderList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityHomeViewModel : ViewModel() {

    lateinit var reminderListData: MutableLiveData<ReminderList>
    lateinit var reminderAdapter: ReminderAdapter

    init {
        reminderListData = MutableLiveData()
        reminderAdapter = ReminderAdapter()
    }

    fun getAdapter(): ReminderAdapter
    {
        return reminderAdapter
    }

    fun setAdapter(data:ArrayList<ReminderItem>)
    {
        reminderAdapter.setDataList(data)
        reminderAdapter.notifyDataSetChanged()
    }

    fun getReminderListObserver(): MutableLiveData<ReminderList>
    {
        return reminderListData
    }

     fun getReminderAPI(mobile: String)
    {
        val retroInstance = RetrofitInstance.getRetrofitInstance().create(UserInterface::class.java)
        val call = retroInstance.getReminder(mobile)

        call.enqueue(object : Callback<ReminderList> {
            override fun onFailure(call: Call<ReminderList>, t: Throwable) {
                reminderListData.postValue(null)
            }

            override fun onResponse(call: Call<ReminderList>, response: Response<ReminderList>) {
                if (response.isSuccessful) {
                    reminderListData.postValue(response.body())
                } else {
                    reminderListData.postValue(null)
                }
            }
        })
    }
}