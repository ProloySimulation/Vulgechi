package com.xossapp.vulegechi.signUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.xossapp.vulegechi.R
import com.xossapp.vulegechi.network.RetrofitInstance
import com.xossapp.vulegechi.network.UserInterface
import com.xossapp.vulegechi.databinding.ActivitySignUpBinding
import com.xossapp.vulegechi.home.ActivityHome
import com.xossapp.vulegechi.sharedPreference.PreferencesProvider
import com.xossapp.vulegechi.utils.Constants
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.concurrent.TimeUnit

class ActivitySignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mobileNumber:String

    private lateinit var mCallbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var mVerificationId:String? = null
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var preferencesProvider: PreferencesProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)

        preferencesProvider = PreferencesProvider(applicationContext)
        preferencesProvider.putString(Constants.KEY_MOBILE_NUMBER,"notFinished")

        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.apply {
            btnSignUp.setOnClickListener {

                mobileNumber = etSignUpMobile.text.toString()

               // Demo test
                if(mobileNumber.equals("01987982903"))
                {
                    val intent = Intent(this@ActivitySignUp, ActivityHome::class.java)
                    startActivity(intent)
                }

                else if(checkMobileNumber())
                {
                    progressBarSignUp.visibility = View.VISIBLE
                    startPhoneNumberVerification(mobileNumber)
                }
            }
        }


        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                binding.progressBarSignUp.visibility = View.GONE
                Toast.makeText(this@ActivitySignUp,"Check Your Internet Connection",Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                mVerificationId = verificationId
                Toast.makeText(this@ActivitySignUp,"Verification Code Sent",Toast.LENGTH_SHORT).show()
                nextActivity(mVerificationId!!)
            }
        }
    }

    private fun checkMobileNumber():Boolean
    {
        val subNumber = mobileNumber.subSequence(0,3)
        if(subNumber.equals("018")||subNumber.equals("016"))
        {
            return true
        }
        else
        {
            binding.etSignUpMobile.setError("Only Airtel/Robi NUmber")
            return false
        }
    }

    private fun startPhoneNumberVerification(phone:String)
    {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber("+88"+phone)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun nextActivity(verificationId:String) {

        binding.progressBarSignUp.visibility = View.GONE

        val intent = Intent(this, ActivityOtp::class.java)
        intent.putExtra(Constants.KEY_VERIFICATION_ID,verificationId)
        intent.putExtra(Constants.KEY_MOBILE_NUMBER,mobileNumber)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left)
    }
}