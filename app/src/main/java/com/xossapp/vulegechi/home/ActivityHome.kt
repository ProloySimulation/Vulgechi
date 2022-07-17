package com.xossapp.vulegechi.home

import android.app.Dialog
import android.content.ClipData
import android.graphics.drawable.ClipDrawable.VERTICAL
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.xossapp.vulegechi.BR
import com.xossapp.vulegechi.newReminder.data.ReminderList
import com.xossapp.vulegechi.R
import com.xossapp.vulegechi.databinding.ActivityHomeBinding
import com.xossapp.vulegechi.databinding.SignupDialogBinding
import com.xossapp.vulegechi.network.RetrofitInstance
import com.xossapp.vulegechi.network.UserInterface
import com.xossapp.vulegechi.sharedPreference.PreferencesProvider
import com.xossapp.vulegechi.signUp.Data.user
import com.xossapp.vulegechi.utils.Constants
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.MutableLiveData

import android.content.ClipData.Item
import android.content.Intent
import com.xossapp.vulegechi.CheckInternetConnection
import com.xossapp.vulegechi.newReminder.ActivityReminder
import com.xossapp.vulegechi.newReminder.data.ReminderItem
import com.xossapp.vulegechi.wish.activity.ActivityWish


class ActivityHome : AppCompatActivity(),ReminderAdapter.ItemClickListener{

    private lateinit var retrofitService: UserInterface
    private lateinit var date:SimpleDateFormat
    private lateinit var checkInternetConnection: CheckInternetConnection

    private lateinit var preferencesProvider: PreferencesProvider

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)

         preferencesProvider = PreferencesProvider(applicationContext)
         val viewModel = getReminder()
         setUpBinding(viewModel)

         retrofitService = RetrofitInstance
             .getRetrofitInstance()
             .create(UserInterface::class.java)

         callNetworkConnection()
     }

    override fun onBackPressed() {

        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun callNetworkConnection()
    {
        checkInternetConnection = CheckInternetConnection(application)
        checkInternetConnection.observe(this,{isConnected->
            if(isConnected)
            {
                check_sub()
            }
            else
            {
                Toast.makeText(this,"Check Internet Connection",Toast.LENGTH_SHORT).show()
            }
        })
    }

     fun setUpBinding(viewModel: ActivityHomeViewModel)
    {
        val acitivityHomeBinding: ActivityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        acitivityHomeBinding.setVariable(BR.viewModel,viewModel)
        acitivityHomeBinding.executePendingBindings()

        acitivityHomeBinding.rvReminders.apply {
            layoutManager = LinearLayoutManager(this@ActivityHome)
            val decoration = DividerItemDecoration(this@ActivityHome, LinearLayoutManager.VERTICAL)
            addItemDecoration(decoration)
        }

        acitivityHomeBinding.fabNewRemineder.setOnClickListener {
             val intent = Intent(this, ActivityReminder::class.java)
             intent.putExtra(Constants.KEY_REMINDER_CREATE,Constants.KEY_REMINDER_CREATE)
             startActivity(intent)
             overridePendingTransition(R.anim.smooth_fade_in, R.anim.smooth_fade_out)
         }

        acitivityHomeBinding.cvNewReminder.setOnClickListener {
            val intent = Intent(this, ActivityReminder::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.smooth_fade_in, R.anim.smooth_fade_out)
        }

        acitivityHomeBinding.cvWishMessage.setOnClickListener {
            val intent = Intent(this, ActivityWish::class.java)
            startActivity(intent)
        }
    }


     fun getReminder(): ActivityHomeViewModel
    {
        val mobile = preferencesProvider.getString(Constants.KEY_MOBILE_NUMBER).toString()
        val viewModel = ViewModelProviders.of(this).get(ActivityHomeViewModel::class.java)

        viewModel.getReminderListObserver().observe(this,Observer{

            if(it!=null)
            {
                viewModel.setAdapter(it.set_remind)
//                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
            }
            else
            {
                Toast.makeText(this, "Error in fetching data", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.getReminderAPI(mobile)

        return viewModel
    }

    fun check_sub()
    {
        val mobile = preferencesProvider.getString(Constants.KEY_MOBILE_NUMBER).toString()
        val postResponse: LiveData<Response<user>> = liveData {

            val response: Response<user> = retrofitService.check_sub(mobile)
            emit(response)

        }

        postResponse.observe(this, Observer {

            val receivedUsersInfo = it.body()

            if(receivedUsersInfo?.response.equals("This number is registered"))
            {
//                Toast.makeText(this,"Registered", Toast.LENGTH_SHORT).show()
            }
            else
            {
                subscribe_number()
            }

        })
    }

    fun sub_dialog()
    {
        val dialog = Dialog(this)
        val signupDialogBinding:SignupDialogBinding = SignupDialogBinding.inflate(layoutInflater)
        dialog.setContentView(signupDialogBinding.root)
        dialog.show()

        signupDialogBinding.confirmDialogButton.setOnClickListener {
            dialog.dismiss()
        }

    }

    fun subscribe_number()
    {
        val mobile = preferencesProvider.getString(Constants.KEY_MOBILE_NUMBER).toString()
        val postResponse: LiveData<Response<user>> = liveData {

            val response: Response<user> = retrofitService.subscription(mobile)
            emit(response)

        }

        postResponse.observe(this, Observer {

            val receivedUsersInfo = it.body()

            if(receivedUsersInfo?.response.equals("Send request successfully!!!"))
            {
                sub_dialog()
            }
            else
            {
                Toast.makeText(this,"Check Internet Connection", Toast.LENGTH_SHORT).show()
                sub_dialog()
            }

        })
    }

    override fun onReminderClick(reminderItem: ReminderItem) {
        Toast.makeText(this,"Hello",Toast.LENGTH_SHORT).show()
    }
}