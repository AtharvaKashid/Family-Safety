package com.example.fmsafety

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.fmsafety.databinding.FragmentGuardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class GuardFragment : Fragment(),OnActionClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var onActionClickListener:OnActionClickListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnActionClickListener) {

             onActionClickListener=this@GuardFragment

        } else {
            throw RuntimeException("$context must implement OnActionClickListener")
        }
    }

    lateinit var binding: FragmentGuardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGuardBinding.inflate(inflater, container, false)
        binding.sendInvite.setOnClickListener {
            sendInvite()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInvites()
    }

    private fun getInvites() {

        val database = FirebaseDatabase.getInstance()
        val currentUserEmail =
            FirebaseAuth.getInstance().currentUser?.email.toString().replace('@', '-').replace('.','!')
        val invitesRef = database.reference.child("Family").child(currentUserEmail).child("invites")
        invitesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: ArrayList<String> = ArrayList()
                for (itemSnapshot in snapshot.children) {
                    val inviteStatus =
                        itemSnapshot.child("invite_status").getValue(Long::class.java)
                    if (inviteStatus == 0L) {
                        list.add(itemSnapshot.key!!)
                    }
                }

                val adapter = onActionClickListener?.let { InviteMailAdapter(list, it) }
                binding.inviteRecycler.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that occur during the database operation
            }
        })
    }

    private fun sendInvite() {
        val mail = binding.inviteMail.text.toString()
        val database = FirebaseDatabase.getInstance().reference
        val data = hashMapOf(
            "invite_status" to 0
        )

        val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        database.child("Family").child(mail.replace('@', '-').replace('.','!')).child("invites")
            .child(senderMail.replace('@', '-').replace('.','!')).setValue(data).addOnSuccessListener {

                Toast.makeText(requireContext(), "Invite Send Successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {

        }
        binding.inviteMail.text.clear()

    }

    companion object {

        @JvmStatic
        fun newInstance() = GuardFragment()
    }

    override fun onAcceptClick(mail: String) {
        val database = FirebaseDatabase.getInstance().reference
        val data = hashMapOf(
            "invite_status" to 1
        )

        val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        database.child("Family").child(senderMail.replace('@', '-').replace('.','!')).child("invites")
            .child(mail.replace('@', '-').replace('.','!')).setValue(data).addOnSuccessListener {
            Toast.makeText(requireContext(),"Invite Accepted",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {

        }
    }

    override fun onDenyClick(mail: String) {
        val database = FirebaseDatabase.getInstance().reference
        val data = hashMapOf(
            "invite_status" to -1
        )
        val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        database.child("Family").child(senderMail.replace('@', '-').replace('.','!')).child("invites")
            .child(mail.replace('@', '-').replace('.','!')).setValue(data).addOnSuccessListener {
                Toast.makeText(requireContext(),"Invite Denied",Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {

        }
    }
}