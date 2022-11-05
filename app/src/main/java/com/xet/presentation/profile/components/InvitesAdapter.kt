package com.xet.presentation.profile.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xet.R
import com.xet.domain.model.Contact
import com.xet.domain.model.FriendshipStatus

class InvitesAdapter(
    private val contacts: List<Contact>,
    private val context: Context,
    private val callback: (String, Boolean) -> Unit,
): RecyclerView.Adapter<InvitesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(contact: Contact, callback: (String, Boolean) -> Unit) {
            val displayName: TextView = itemView.findViewById(R.id.profileInviteItemName)
            val refuseBtn: ImageButton = itemView.findViewById(R.id.profileInviteRefuse)
            val acceptBtn: ImageButton = itemView.findViewById(R.id.profileInviteAccept)

            if (contact.friendshipStatus == FriendshipStatus.SENT_FRIEND_REQUEST) {
                refuseBtn.setOnClickListener {
                    callback(contact.userId, false)
                }
                acceptBtn.setOnClickListener {
                    callback(contact.userId, true)
                }
            }

            displayName.text = contact.displayName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.invite_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]

        holder.bindView(contact, callback)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

}