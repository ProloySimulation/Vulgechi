package com.xossapp.vulegechi.storyboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.xossapp.vulegechi.databinding.ActivitySplashBinding

class ActivitySplash : AppCompatActivity() {

    private lateinit var binding:ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, com.xossapp.vulegechi.R.layout.activity_splash)

    }
}