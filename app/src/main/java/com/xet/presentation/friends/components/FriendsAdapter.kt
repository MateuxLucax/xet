package com.xet.presentation.friends.components

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xet.R
import com.xet.domain.model.Friend
import com.xet.presentation.chat.ChatActivity
import com.xet.presentation.signup.SignUpActivity

class FriendsAdapter(
    private val friends: List<Friend>,
    private val context: Context
): RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(friend: Friend) {
            val displayName: TextView = itemView.findViewById(R.id.contact_list_item_name)
            val status: TextView = itemView.findViewById(R.id.contact_list_item_status)
            val profileImage: TextView = itemView.findViewById(R.id.contact_list_item_picture)

            displayName.text = friend.displayName
            friend.status?.let {
                status.setText(it.toResourceString())
                status.setTextColor(itemView.context.getColor(it.toColor()))
            }
            profileImage.text = friend.displayName[0].toString()

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ChatActivity::class.java)
                intent.putExtra(ChatActivity.FRIEND_ID, friend.userId)
                intent.putExtra(ChatActivity.FRIEND_NAME, friend.displayName)
                intent.putExtra(ChatActivity.FRIEND_USER_NAME, friend.username)

                itemView.context.startActivity(intent)
            }
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