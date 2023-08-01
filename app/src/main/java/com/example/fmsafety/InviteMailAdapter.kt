package com.example.fmsafety

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fmsafety.databinding.ItemInviteMailBinding


class InviteMailAdapter(
    val listInvites: List<String>,
    val onActionClickListener: com.example.fmsafety.OnActionClickListener
) :
    RecyclerView.Adapter<InviteMailAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InviteMailAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = ItemInviteMailBinding.inflate(inflater, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: InviteMailAdapter.ViewHolder, position: Int) {

        val item = listInvites[position]
        holder.name.text = item

        holder.accept.setOnClickListener {
           onActionClickListener.onAcceptClick(item)
        }

        holder.deny.setOnClickListener {
            onActionClickListener.onDenyClick(item)
        }

    }

    override fun getItemCount(): Int {
        return listInvites.size
    }

    class ViewHolder(private val item: ItemInviteMailBinding) : RecyclerView.ViewHolder(item.root) {
        val name = item.mail
        val accept = item.accept
        val deny = item.deny

    }
    interface OnActionClickListener {
        fun onAcceptClick(inviteId: String)
        fun onDenyClick(item:String)

    }



}