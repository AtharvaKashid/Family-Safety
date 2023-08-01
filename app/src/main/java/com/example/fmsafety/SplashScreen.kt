package com.example.fmsafety

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isUserLoggedIn =   SharedPref.getBoolean(PrefConstants.IS_USER_LOGGED_IN)

        if(isUserLoggedIn){
            startActivity(Intent(this@SplashScreen,MainActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this@SplashScreen,LoginActivity::class.java))
            finish()
        }

    }
}