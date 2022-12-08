package com.xet.presentation.chat.components

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xet.R
import com.xet.domain.model.Message

class ChatAdapter(
    private val messages: List<Message>,
    private val context: Context,
    private val loadMoreCallback: ((Number)->Unit)
): RecyclerView.Adapter<ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_bubble, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        if (position == 0) loadMoreCallback(messages.first().id.toLong())

        holder.bind(messages[position])
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemId(position: Int): Long {
        return messages[position].id.toLong()
    }

    override fun onViewDetachedFromWindow(holder: ChatViewHolder) {
        val message = holder.msg
        if (message?.fileReference != null) {
            holder.stop()
        }
        super.onViewDetachedFromWindow(holder)
    }
}