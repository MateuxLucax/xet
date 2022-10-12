package com.xet.presentation.search.components

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

class SearchAdapter(
    private val contacts: List<Contact>,
    private val context: Context,
    private val friendCallback: (userId: String) -> Void,
    private val sendInviteCallBack: () -> Void
): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(
            contact: Contact,
            friendCallback: (userId: String) -> Void,
            sendInviteCallBack: () -> Void
        ) {
            val displayName: TextView = itemView.findViewById(R.id.contact_search_item_name)
            val actionBtn: ImageButton = itemView.findViewById(R.id.contact_search_item_btn)

            if (contact.friendshipStatus != null) {
                actionBtn.setImageDrawable(itemView.context.getDrawable(contact.friendshipStatus.toIcon()))
                actionBtn.contentDescription = contact.friendshipStatus.toDescription().toString()
                actionBtn.isActivated = false
                if (contact.friendshipStatus == FriendshipStatus.FRIEND) {
                    actionBtn.setOnClickListener {
                        friendCallback(contact.userId)
                    }
                }
            } else {
                actionBtn.setOnClickListener {
                    sendInviteCallBack()
                }
            }
            displayName.text = contact.displayName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.friend_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]

        holder.bindView(contact, friendCallback, sendInviteCallBack)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

}