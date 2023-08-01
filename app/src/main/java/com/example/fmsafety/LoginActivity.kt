package com.example.fmsafety

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    lateinit var googlesignInClient:GoogleSignInClient
    private var RC_SIGN_IN= 89
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("571043047652-mg7003ktfcj9lr9q3p3i3a9s4u1o1fgp.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googlesignInClient=GoogleSignIn.getClient(this,gso)

    }
    fun signIn(view:android.view.View){
        val signInIntent=googlesignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account=task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken)
            }catch (e:ApiException){
                Log.d("Error90","Google Sign in Failed:"+e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val auth=FirebaseAuth.getInstance()
        val credential=GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener(this){task->
            if(task.isSuccessful){
//                SharedPref.init(this)
                SharedPref.putBoolean(PrefConstants.IS_USER_LOGGED_IN,true)
                Log.d("Fire89","signInWithCredential:success")
                val user=auth.currentUser
                startActivity(Intent(this,MainActivity::class.java))
//                Log.d("Fire89","signInWithCredential:${user?.displayName}")
                if (user != null) {
                    user.displayName?.let { SharedPref.putString(PrefConstants.USER_NAME, it) }
                    if(user.photoUrl!=null) {
                        user.photoUrl.let { SharedPref.putStringUri(PrefConstants.PHOTO_URL, it) }
                    }
                }

            }else{
                Log.d("Fire90","signInWithCredential:failed")
            }

        }
    }


}