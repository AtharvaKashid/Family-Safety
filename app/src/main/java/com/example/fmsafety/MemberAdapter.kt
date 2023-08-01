package com.example.fmsafety

import android.Manifest.permission.CALL_PHONE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fmsafety.databinding.ItemLayoutBinding

class MemberAdapter(var context: android.content.Context,private val listMembers:List<MemberModel>): RecyclerView.Adapter<MemberAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewtype:Int):MemberAdapter.ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val item=ItemLayoutBinding.inflate(inflater,parent,false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder:MemberAdapter.ViewHolder,position:Int){
        val item = listMembers[position]
        holder.name.text=item.name
        holder.address.text=item.address
        holder.battery.text=item.battery
        holder.distance.text=item.distance
        holder.call.setOnClickListener{
            if(listMembers[position]==listMembers[0]){
                if(ContextCompat.checkSelfPermission(context,android.Manifest.permission.CALL_PHONE
                    ) == PackageManager.PERMISSION_GRANTED){
                    val phoneNumber="9869310932"
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                   context.startActivity(intent)

                }else{
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(android.Manifest.permission.CALL_PHONE),
                        1 // You can use any request code you want here
                    )
                }


            }else if(listMembers[position]==listMembers[1]){
                if(ContextCompat.checkSelfPermission(context,android.Manifest.permission.CALL_PHONE
                    ) == PackageManager.PERMISSION_GRANTED){
                    val phoneNumber="9769650818"
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                    context.startActivity(intent)

                }else{
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(android.Manifest.permission.CALL_PHONE),
                        1 // You can use any request code you want here
                    )
                }


            }else if(listMembers[position]==listMembers[2]){
                if(ContextCompat.checkSelfPermission(context,android.Manifest.permission.CALL_PHONE
                    ) == PackageManager.PERMISSION_GRANTED){
                    val phoneNumber="9769650818"
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                    context.startActivity(intent)

                }else{
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(android.Manifest.permission.CALL_PHONE),
                        1 // You can use any request code you want here
                    )
                }


            }else if(listMembers[position]==listMembers[3]){
                if(ContextCompat.checkSelfPermission(context,android.Manifest.permission.CALL_PHONE
                    ) == PackageManager.PERMISSION_GRANTED){
                    val phoneNumber="8451952564"
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                    context.startActivity(intent)

                }else{
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(android.Manifest.permission.CALL_PHONE),
                        1 // You can use any request code you want here
                    )
                }


            }
        }


    }


    override fun getItemCount(): Int {
        return listMembers.size
    }

    class ViewHolder(private val item: ItemLayoutBinding): RecyclerView.ViewHolder(item.root) {

        val ImageUser = item.imgUser
//        val card = item.findViewById<CardView>(R.id.card)
        val name=item.name
        val address = item.address

        val battery = item.batteryPercent
        val distance = item.distanceValue
       val call = item.imgCall
    }

}


