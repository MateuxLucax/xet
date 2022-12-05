package com.xet.presentation.chat.components

import android.content.Context
import android.graphics.text.TextRunShaper
import android.media.MediaPlayer
import android.opengl.Visibility
import android.text.format.DateFormat.getDateFormat
import android.text.format.DateFormat.getLongDateFormat
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.xet.R
import com.xet.data.Utils
import com.xet.domain.model.FileType
import com.xet.domain.model.Message
import java.text.DateFormat
import java.time.ZoneId
import java.util.*
import kotlin.math.ceil

class ChatAdapter(
    private val messages: List<Message>,
    private val context: Context
): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private fun bindData(message: Message) {
            val dateText: TextView = itemView.findViewById(R.id.chatBubbleDate)

            if (message.text != null) bindText(message)
            else if (message.file != null) bindFile(message)

            dateText.text = Utils.formatDate(message.sentAt)
        }

        private fun bindText(message: Message) {
            val messageText: TextView = itemView.findViewById(R.id.chatBubbleText)
            messageText.text = message.text
        }

        private fun bindFile(message: Message) {
            when (message.fileType) {
                FileType.AUDIO -> BindAudio(message, itemView).bind()
                else -> { Log.i("chat_adapter", "unsupported file type: ${message.fileType}") }
            }
        }

        fun bindViewRight(message: Message) {
            bindData(message)
            itemView.background = AppCompatResources.getDrawable(itemView.context, R.drawable.chat_bubble_right)
            itemView.margin(left = 48F, right = 0F)
        }

        fun bindViewLeft(message: Message) {
            bindData(message)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_bubble, parent, false)
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

fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = dpToPx(this) }
        top?.run { topMargin = dpToPx(this) }
        right?.run { rightMargin = dpToPx(this) }
        bottom?.run { bottomMargin = dpToPx(this) }
    }
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)

fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
