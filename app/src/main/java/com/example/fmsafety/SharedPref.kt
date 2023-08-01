package com.example.fmsafety

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

object SharedPref {

    private const val NAME="FMPrefs"
    private lateinit var preferences:SharedPreferences
    private val IS_FIRST_RUn_PREF =Pair("is_first_run",false)
    fun init(context:Context){
        preferences=context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    fun putBoolean(key:String,value:Boolean){

        preferences.edit().putBoolean(key,value).commit()

    }

    fun getBoolean(key:String):Boolean{
       return preferences.getBoolean(key,false)
    }


    fun putString(key: String, value: String){
        preferences.edit().putString(key,value).commit()
    }

    fun getString(key: String): String? {
        return preferences.getString(key,null)
    }

    fun putStringUri(key:String, value: Uri?){
        preferences.edit().putString(key, value.toString()).commit()
    }
    fun getStringUri(key: String): String? {
        return preferences.getString(key,null)
    }

}