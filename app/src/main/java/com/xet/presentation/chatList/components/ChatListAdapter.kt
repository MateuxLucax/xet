package com.xet.presentation.chatList.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xet.R
import com.xet.domain.model.Contact

class ChatListAdapter(
    private val contacts: List<Contact>,
    private val context: Context
): RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(contact: Contact) {
            val displayName: TextView = itemView.findViewById(R.id.chat_list_item_name)
            val lastMessage: TextView = itemView.findViewById(R.id.chat_list_item_last_message)
            val status: TextView = itemView.findViewById(R.id.chat_list_item_status)
            val profileImage: TextView = itemView.findViewById(R.id.chat_list_item_picture)

            lastMessage.text = contact.lastMessage ?: ""
            displayName.text = contact.displayName
            status.setText(contact.status.toResourceString())
            status.setTextColor(contact.status.toColor())
            profileImage.text = contact.displayName[0].toString();
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]

        holder.bindView(contact)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }


}