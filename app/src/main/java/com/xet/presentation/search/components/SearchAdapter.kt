package com.xet.presentation.search.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.xet.R
import com.xet.domain.model.Contact
import com.xet.domain.model.FriendshipStatus

class SearchAdapter(
    private val contacts: List<Contact>,
    private val context: Context,
    private val friendCallback: (String) -> Unit,
    private val sendInviteCallback: (String) -> Unit
): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(
            contact: Contact,
            friendCallback: (String) -> Unit,
            sendInviteCallBack: (String) -> Unit
        ) {
            val displayName: TextView = itemView.findViewById(R.id.contact_search_item_name)
            val actionBtn: ImageButton = itemView.findViewById(R.id.contact_search_item_btn)

            actionBtn.setImageDrawable(AppCompatResources.getDrawable(itemView.context, contact.friendshipStatus.toIcon()))
            actionBtn.contentDescription = contact.friendshipStatus.toDescription().toString()
            actionBtn.isActivated = false

            when (contact.friendshipStatus) {
                FriendshipStatus.IS_FRIEND -> actionBtn.setOnClickListener { friendCallback(contact.userId) }
                FriendshipStatus.NO_FRIEND_REQUEST -> actionBtn.setOnClickListener{ sendInviteCallBack(contact.userId) }
                else -> actionBtn.isEnabled = false
            }

            displayName.text = contact.displayName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.contact_search_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bindView(contact, friendCallback, sendInviteCallback)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

}