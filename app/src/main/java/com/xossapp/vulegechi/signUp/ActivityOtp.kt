package com.xossapp.vulegechi.signUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.xossapp.vulegechi.R
import com.xossapp.vulegechi.databinding.ActivityOtpBinding
import com.xossapp.vulegechi.home.ActivityHome
import com.xossapp.vulegechi.network.RetrofitInstance
import com.xossapp.vulegechi.network.UserInterface
import com.xossapp.vulegechi.sharedPreference.PreferencesProvider
import com.xossapp.vulegechi.signUp.Data.user
import com.xossapp.vulegechi.utils.Constants
import retrofit2.Response

class ActivityOtp : AppCompatActivity() {

    private lateinit var binding: ActivityOtpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var preferencesProvider:PreferencesProvider
    private lateinit var mobileNumber:String
    private lateinit var retrofitService: UserInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp)

        firebaseAuth = FirebaseAuth.getInstance()
        preferencesProvider = PreferencesProvider(applicationContext)

        val verficiationId = intent.getStringExtra(Constants.KEY_VERIFICATION_ID)
        mobileNumber = intent.getStringExtra(Constants.KEY_MOBILE_NUMBER).toString()

        binding.btnVerify.setOnClickListener {

            var otp: String = binding.pinViewotp.text.toString()

            if (verficiationId != null) {
                verifyPhoneNumberWithCode(verficiationId,otp)
            }
            overridePendingTransition(R.anim.smooth_fade_in,R.anim.smooth_fade_out) // animation
        }
    }

    private fun verifyPhoneNumberWithCode(verificationId:String,code:String)
    {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential)
    {
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                login(mobileNumber)
            }
            .addOnFailureListener {

            }
    }

    private fun login(mobileNumber:String) {

        retrofitService = RetrofitInstance
            .getRetrofitInstance()
            .create(UserInterface::class.java)

        val album = user(mobileNumber, null,null,null)
        val postResponse: LiveData<Response<user>> = liveData {

            val response: Response<user> = retrofitService.login(album)
            emit(response)

        }

        postResponse.observe(this, Observer {

            val receivedUsersInfo = it.body()

            if(receivedUsersInfo?.response.equals("Successfully!"))
            {
                Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()

                preferencesProvider.putString(Constants.KEY_MOBILE_NUMBER,mobileNumber)
                val intent = Intent(this, ActivityHome::class.java)
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this,"Try Again",Toast.LENGTH_SHORT).show()
            }

        })
    }
}