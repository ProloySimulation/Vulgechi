package com.xossapp.vulegechi.storyboard

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.xossapp.vulegechi.R
import com.xossapp.vulegechi.sharedPreference.PreferencesProvider
import com.xossapp.vulegechi.utils.Constants
import kotlinx.android.synthetic.main.fragment_splash.view.*


class SplashFragment : Fragment() {

    private lateinit var preferencesProvider: PreferencesProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view =  inflater.inflate(R.layout.fragment_splash, container, false)
        preferencesProvider = PreferencesProvider(requireActivity())

        val animation: Animation = AnimationUtils.loadAnimation(activity, com.xossapp.vulegechi.R.anim.bounce)
        val checkMobile:String = preferencesProvider.getString(Constants.KEY_MOBILE_NUMBER).toString()

        view.imvLogo.startAnimation(animation)
        Handler().postDelayed({

            if(onBoardingFinished() && !checkMobile.equals("notFinished"))
            {
                findNavController().navigate(R.id.action_splashFragment_to_activityHome2)

            }else
            {
                findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
            }
        }, 3000)


        return view
    }

    private fun onBoardingFinished(): Boolean{
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}