package com.example.fmsafety

import android.app.Application

class FamilyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPref.init(this)
    }
}