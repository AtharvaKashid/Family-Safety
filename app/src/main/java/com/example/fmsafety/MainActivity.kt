package com.example.fmsafety

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import com.google.android.gms.location.LocationRequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.fmsafety.GuardFragment.Companion.newInstance
import com.example.fmsafety.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(),OnActionClickListener {

     private val permissions= arrayOf(
         android.Manifest.permission.ACCESS_FINE_LOCATION,
         android.Manifest.permission.CAMERA,
         android.Manifest.permission.READ_CONTACTS
     )

    private val permissionCode=72

    lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        if(isAllPermissionsGranted()){
           if(isLocationEnabled(this)){
               setUpLocationListener()
           }else{
               showGPSNotEnabledDialog(this)
           }
        }else{
            askForPermission()
        }

//      val bottombar=findViewById<BottomNavigationView>(R.id.bottomnavbar)
       binding.bottomnavbar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    inflatefragment(HomeFragment.newInstance())
                }
                R.id.nav_dashboard -> {
                    inflatefragment(GoogleMapsFragment.newInstance())
                }
                R.id.nav_guard -> {
                    inflatefragment(GuardFragment.newInstance())
                }
                R.id.nav_profile -> {
                    inflatefragment(ProfileFragment.newInstance())
                }
            }
            true
        }
        binding.bottomnavbar.selectedItemId = R.id.nav_home

        val auth=FirebaseAuth.getInstance()
        val name= auth.currentUser?.displayName.toString()
        val mail= auth.currentUser?.email.toString()
        val phoneNumber= auth.currentUser?.phoneNumber.toString()

        val database= FirebaseDatabase.getInstance().reference
        val user = hashMapOf(
            "name" to name,
            "email" to mail,
            "phoneNumber" to phoneNumber
        )
        database.child("Family").child(name.toString()).setValue(user).addOnSuccessListener {

        }.addOnFailureListener {

        }

    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
              android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
             android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
//                        Log.d("Location89", "onLocationResult: latitude ${location.latitude}")
//                        Log.d("Location89", "onLocationResult: longitude ${location.longitude}")

                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val name = currentUser?.displayName.toString()


                        val locationData = mutableMapOf<String,Any>(
                            "lat" to location.latitude.toString(),
                            "long" to location.longitude.toString(),
                        )

                        val db=FirebaseDatabase.getInstance().reference
                        db.child("Family").child(name.toString()).setValue(locationData).addOnSuccessListener {

                        }.addOnFailureListener {

                        }
                    }
                }
            },
            Looper.myLooper()
        )
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /**
     * Function to show the "enable GPS" Dialog box
     */
    fun showGPSNotEnabledDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Enable GPS")
            .setMessage("required_for_this_app")
            .setCancelable(false)
            .setPositiveButton("enable_now") { _, _ ->
                context.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }
    fun isAllPermissionsGranted():Boolean{

        for(item in permissions){
           if(ContextCompat.checkSelfPermission(this,item)!=PackageManager.PERMISSION_GRANTED){
               return false
           }

        }
        return true
    }

    private fun askForPermission() {

        ActivityCompat.requestPermissions(this,permissions,permissionCode)
    }

    private fun inflatefragment(newInstance: Fragment) {
        val transaction =supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container   ,  newInstance)
        transaction.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if(requestCode == permissionCode){

            if(allPermissionsGranted()){
                setUpLocationListener()
//                openCamera();
            }else {

            }
        }
    }

    private fun openCamera() {
        val intent=Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }

    private fun allPermissionsGranted(): Boolean {
        for (item in permissions){
            if(ContextCompat.checkSelfPermission(this,item)!=PackageManager.PERMISSION_GRANTED){
                return  false
            }
        }
       return  true
    }

    override fun onAcceptClick(inviteId: String) {
        TODO("Not yet implemented")
    }

    override fun onDenyClick(item: String) {
        TODO("Not yet implemented")
    }

}


