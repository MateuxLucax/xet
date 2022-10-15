package com.xet.presentation.friends.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xet.R
import com.xet.domain.model.Friend

class FriendsAdapter(
    private val friends: List<Friend>,
    private val context: Context
): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(friend: Friend) {
            val displayName: TextView = itemView.findViewById(R.id.contact_list_item_name)
            val lastMessage: TextView = itemView.findViewById(R.id.contact_list_item_last_message)
            val status: TextView = itemView.findViewById(R.id.contact_list_item_status)
            val profileImage: TextView = itemView.findViewById(R.id.contact_list_item_picture)

            lastMessage.text = friend.lastMessage ?: ""
            displayName.text = friend.displayName
            if (friend.status != null) {
                status.setText(friend.status.toResourceString())
                status.setTextColor(itemView.context.getColor(friend.status.toColor()))
            }
            profileImage.text = friend.displayName[0].toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.friend_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = friends[position]

        holder.bindView(friend)
    }

    override fun getItemCount(): Int {
        return friends.size
    }
}