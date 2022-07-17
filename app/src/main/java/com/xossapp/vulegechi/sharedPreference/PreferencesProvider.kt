package com.xossapp.vulegechi.sharedPreference

import android.content.Context
import android.content.SharedPreferences

class PreferencesProvider(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("preferences",0)

    fun putString(key:String,value:String)
    {
        sharedPreferences.edit().putString(key,value).apply()
    }
    fun getString(key: String):String?
    {
        return sharedPreferences.getString(key,null)
    }
}