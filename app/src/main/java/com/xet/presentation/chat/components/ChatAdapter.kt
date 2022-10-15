package com.xet.presentation.chat.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.xet.R
import com.xet.domain.model.Message

class ChatAdapter(
    private val messages: List<Message>,
    private val context: Context
): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindViewRight(message: Message) {
            val messageText: TextView = itemView.findViewById(R.id.messageBubbleText)
            val dateText: TextView = itemView.findViewById(R.id.messageBubbleDate)

            messageText.text = message.message
            dateText.text = message.sentAt

            itemView.background = AppCompatResources.getDrawable(itemView.context, R.drawable.chat_bubble_right)
            itemView.setMargins(48, 0, 0, 0)
        }

        fun bindViewLeft(message: Message) {
            val messageText: TextView = itemView.findViewById(R.id.messageBubbleText)
            val dateText: TextView = itemView.findViewById(R.id.messageBubbleDate)

            messageText.text = message.message
            dateText.text = message.sentAt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.friend_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]

        if (message.isMine) {
            holder.bindViewRight(message)
        } else {
            holder.bindViewLeft(message)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}

fun View.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(left, top, right, bottom)
    layoutParams = params
}
