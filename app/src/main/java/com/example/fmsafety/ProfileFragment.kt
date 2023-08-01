package com.example.fmsafety

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.fmsafety.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
   var txtname: TextView? =null
    lateinit var mContext:Context
    lateinit var binding: FragmentProfileBinding
    private val handler = Handler(Looper.getMainLooper())
    private val finishDelayMillis = 1500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(inflater,container,false)

        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        txtname=requireView().findViewById<TextView>(R.id.user_name)
       binding.userName.let { it ->
            if (it != null) {
                it.text=SharedPref.getString(PrefConstants.USER_NAME)
            }
        }
        val url =SharedPref.getStringUri(PrefConstants.PHOTO_URL).toString()
        if (url!=null) {
            Glide.with(mContext).load(Uri.parse(url)).placeholder(R.drawable.ic_user_color)
                .centerCrop().into(binding.profileImage)
        }
        binding.logout.setOnClickListener {
            SharedPref.putBoolean(PrefConstants.IS_USER_LOGGED_IN,false)
            FirebaseAuth.getInstance().signOut()
            handler.postDelayed(finishRunnable, finishDelayMillis.toLong())
            Toast.makeText(requireContext(),"You have logged out successfully", Toast.LENGTH_SHORT).show()
        }
    }
    private val finishRunnable=Runnable{
        activity?.finish()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()

    }
    override fun onDestroyView() {
        // Remove the callback to avoid leaks
        handler.removeCallbacks(finishRunnable)
        super.onDestroyView()
    }
}