package com.xossapp.vulegechi.wish.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.CalendarViewBindingAdapter.setDate
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.xossapp.vulegechi.BR
import com.xossapp.vulegechi.R
import com.xossapp.vulegechi.databinding.ActivityHomeBinding
import com.xossapp.vulegechi.databinding.ActivityWishBinding
import com.xossapp.vulegechi.databinding.SignupDialogBinding
import com.xossapp.vulegechi.home.ActivityHomeViewModel
import com.xossapp.vulegechi.network.RetrofitInstance
import com.xossapp.vulegechi.network.UserInterface
import com.xossapp.vulegechi.newReminder.ActivityReminderViewModel
import com.xossapp.vulegechi.newReminder.fragment.DatePickerFragment
import com.xossapp.vulegechi.sharedPreference.PreferencesProvider
import com.xossapp.vulegechi.signUp.Data.user
import com.xossapp.vulegechi.utils.Constants
import com.xossapp.vulegechi.wish.data.Wish
import com.xossapp.vulegechi.wish.viewModel.ActivityWishViewModel
import retrofit2.Response

class ActivityWish : AppCompatActivity() {

    private lateinit var binding: ActivityWishBinding
    private lateinit var viewModel: ActivityWishViewModel

    private lateinit var preferencesProvider: PreferencesProvider

    private lateinit var addressMobile: String
    private lateinit var wishDate:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferencesProvider = PreferencesProvider(applicationContext)
        addressMobile = preferencesProvider.getString(Constants.KEY_MOBILE_NUMBER).toString()

        viewModel = getWish()
        setup(viewModel)

    }

    private fun setWish() {

        val wishMessage: String = binding.etWishMessage.text.toString()
        val senderNumber: String = binding.etWishMobile.text.toString()

        if(wishMessage.isNullOrBlank() || senderNumber.isNullOrBlank() || !::wishDate.isInitialized)
        {
            Toast.makeText(applicationContext,"Please Provide All Information",Toast.LENGTH_SHORT).show()
        }
        else
        {
            if(checkMobileNumber(senderNumber))
            {
                val setWish = Wish(addressMobile, wishDate, wishMessage, senderNumber)

                viewModel.setWish(setWish)

                viewModel.createWishObserver()
                    .observe(this@ActivityWish, androidx.lifecycle.Observer {
                        if (it == null) {
//                    binding.progrssBarReminder.visibility = View.GONE
                            Toast.makeText(
                                applicationContext,
                                "Failed to set Wish",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
//                    activityReminderBinding.progrssBarReminder.visibility = View.GONE
                            Toast.makeText(
                                applicationContext,
                                "Wish Set Successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.apply {
                                etWishMessage.text?.clear()
                                etWishMobile.text?.clear()
                            }
                            getWish()
                        }
                    })
            }

        }
    }

    fun setup(viewModel: ActivityWishViewModel)
    {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wish)
        binding.setVariable(BR.viewModel,viewModel)
        binding.executePendingBindings()

        binding.rvWishList.apply {
            layoutManager = LinearLayoutManager(this@ActivityWish)
            val decoration = DividerItemDecoration(this@ActivityWish, LinearLayoutManager.VERTICAL)
            addItemDecoration(decoration)
        }
        binding.btnWishSet.setOnClickListener {
            check_sub()
        }
        binding.tvAddWishDate.setOnClickListener {
            setCalenderDate()
        }

        binding.imvWishReminder.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun getWish():ActivityWishViewModel
    {

        viewModel = ViewModelProviders.of(this).get(ActivityWishViewModel::class.java)
        viewModel.getWishListObserver().observe(this, Observer{

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
        viewModel.getWishList(addressMobile)
        return viewModel
    }

    private fun setCalenderDate()
    {
        val datePickerFragment = DatePickerFragment()
        val supportFragmentManager = supportFragmentManager

        supportFragmentManager.setFragmentResultListener(
            "REQUEST_KEY",
            this
        ) { resultKey, bundle ->
            if (resultKey == "REQUEST_KEY") {
                val date = bundle.getString("SELECTED_DATE")
                binding.tvAddWishDate.text = date
                wishDate = date.toString()
            }
        }

        // show
        datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
    }

    fun check_sub()
    {
        val retrofitService:UserInterface = RetrofitInstance
            .getRetrofitInstance()
            .create(UserInterface::class.java)

        val postResponse: LiveData<Response<user>> = liveData {

            val response: Response<user> = retrofitService.check_sub(addressMobile)
            emit(response)

        }

        postResponse.observe(this, Observer {

            val receivedUsersInfo = it.body()

            if(receivedUsersInfo?.response.equals("This number is registered"))
            {
                setWish()
            }
            else
            {
                subscribe_number()
            }

        })
    }

    fun subscribe_number()
    {
        val retrofitService:UserInterface = RetrofitInstance
            .getRetrofitInstance()
            .create(UserInterface::class.java)

        val postResponse: LiveData<Response<user>> = liveData {

            val response: Response<user> = retrofitService.subscription(addressMobile)
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
            }

        })
    }

    fun sub_dialog()
    {
        val dialog = Dialog(this)
        val signupDialogBinding: SignupDialogBinding = SignupDialogBinding.inflate(layoutInflater)
        dialog.setContentView(signupDialogBinding.root)
        dialog.show()

        signupDialogBinding.confirmDialogButton.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun checkMobileNumber(mobileNumber:String):Boolean
    {
        val subNumber = mobileNumber.subSequence(0,3)
        if(subNumber.equals("018")||subNumber.equals("016"))
        {
            return true
        }
        else
        {
            binding.etWishMobile.setError("Only Airtel/Robi NUmber")
            return false
        }
    }
}