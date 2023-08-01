package com.example.fmsafety

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.provider.ContactsContract.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fmsafety.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class
HomeFragment : Fragment() {
    lateinit var InviteAdapter:InviteAdapter
    lateinit var mContext: Context

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val listContacts:ArrayList<ContactModel> = ArrayList()
    private val handler = Handler(Looper.getMainLooper())
    private val finishDelayMillis = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }

    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding=FragmentHomeBinding.inflate(inflater,container,false)
         return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listMember = listOf<MemberModel>(
            MemberModel(
                "Dinkar Kashid",
                "PH ||,E1 29 C1,Sector 10,Nerul-400705",
                "90%",
                "220"
            ),
            MemberModel(
                "Vaishali Kashid",
                "PH ||,E1 29 C1,Sector 10,Nerul-400705",
                "80%",
                "210"
            ),
            MemberModel(
                "Digvijay Kashid",
                "PH ||,E1 29 C1,Sector 10,Nerul-400705",
                "70%",
                "200"
            ),
            MemberModel(
                "Atharv Kashid",
                "PH ||,E1 29 C1,Sector 10,Nerul-400705",
                "60%",
                "190"
            ),
        )

        val adapter = MemberAdapter(mContext,listMember)
//        val recycler = requireView().findViewById<RecyclerView>(R.id.recycler_member)
        binding.recyclerMember.layoutManager = LinearLayoutManager(mContext)
        binding.recyclerMember.adapter= adapter


        Log.d("FetchContact89","fetchContacts: start karne wale hai")
        Log.d("FetchContact89","fetchContacts: coroutine end${listContacts.size}")
        InviteAdapter=InviteAdapter(listContacts)
        fetchDatabaseContacts()
        Log.d("FetchContact89","fetchContacts: end hogaya hai")

        CoroutineScope(Dispatchers.IO).launch {
           Log.d("FetchContact89","fetchContacts: coroutine start")

            insertDatabaseContacts(fetchContacts())

           Log.d("FetchContact89","fetchContacts: coroutine end${listContacts.size}")

       }
//        val listContacts= listOf<ContactModel>(
//            ContactModel("Dinkar Kashid","9869310932"),
//            ContactModel("Vaishali Kashid","9769650818"),
//            ContactModel("Digvijay Kashid","9769650818"),
//            ContactModel("Atharv Kashid","8451952564")
//
//        )


//        val inviteRecycler=requireView().findViewById<RecyclerView>(R.id.recycler_invite)
        binding.recyclerInvite.layoutManager=LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerInvite.adapter=InviteAdapter


//        val threeDots=requireView().findViewById<ImageView>(R.id.icon_menu)
        binding.iconMenu.setOnClickListener{
            SharedPref.putBoolean(PrefConstants.IS_USER_LOGGED_IN,false)
            FirebaseAuth.getInstance().signOut()
            handler.postDelayed(finishRunnable, finishDelayMillis.toLong())

            Toast.makeText(requireContext(),"You have logged out successfully",Toast.LENGTH_SHORT).show()

        }
    }
    private val finishRunnable=Runnable{
        activity?.finish()
    }

    @SuppressLint("NotifyDataSetChanged")
    private  fun fetchDatabaseContacts() {
        val database = FMDatabase.getDatabase(mContext)
         database.contactDao().getAllContacts().observe(viewLifecycleOwner){
             listContacts.clear()
             listContacts.addAll(it)
             InviteAdapter.notifyDataSetChanged()
         }
    }

    private suspend fun insertDatabaseContacts(listContacts: ArrayList<ContactModel>) {

        val database = FMDatabase.getDatabase(mContext)
        database.contactDao().insertAll(listContacts)

    }

    @SuppressLint("Recycle", "Range")
    private fun fetchContacts(): ArrayList<ContactModel> {

        val cr=mContext.contentResolver
        val cursor = cr.query(Contacts.CONTENT_URI,null,null,null)

        val listOfContacts:ArrayList<ContactModel> = ArrayList()
        if(cursor!=null && cursor.count > 0){
            while(cursor!=null && cursor.moveToNext()){
                val id=cursor.getString(cursor.getColumnIndex(Contacts._ID))
                val name=cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME))
                val hasPhoneNumber=cursor.getInt(cursor.getColumnIndex(Contacts.HAS_PHONE_NUMBER))

                if(hasPhoneNumber>0){
                    val pCur=cr.query(
                        CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", arrayOf(id),"")
                   if(pCur !=null && pCur.count > 0){

                       while(pCur!=null  && pCur.moveToNext()){

                           val phoneNum=pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            listOfContacts.add(ContactModel(name,phoneNum))
                       }
                       pCur.close()
                   }
                }

            }
            if(cursor!=null){
                cursor.close()
            }
        }

        return listOfContacts

    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment();
    }
    override fun onDestroyView() {
        // Remove the callback to avoid leaks
        handler.removeCallbacks(finishRunnable)
        super.onDestroyView()
    }
}