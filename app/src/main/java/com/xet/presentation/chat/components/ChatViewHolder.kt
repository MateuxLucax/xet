package com.xet.presentation.chat.components

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
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
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val dateText: TextView = itemView.findViewById(R.id.chatBubbleDate)
    private val text: TextView = itemView.findViewById(R.id.chatBubbleText)
    private val container: LinearLayout = itemView.findViewById(R.id.chatBubbleAudioContainer)
    private val btn: ImageButton = itemView.findViewById(R.id.chatMessageBtn)
    private val seekbar: SeekBar = itemView.findViewById(R.id.audioSeekbar)
    private val player = MediaPlayer()
    private lateinit var runnable: Runnable

    fun bind(message: Message) {
        if (message.isMine) {
            itemView.background = AppCompatResources.getDrawable(itemView.context, R.drawable.chat_bubble_right)
            itemView.margin(left = 48F, right = 0F)
        } else {
            itemView.background = AppCompatResources.getDrawable(itemView.context, R.drawable.chat_bubble_left)
            itemView.margin(left = 0F, right = 48F)
        }

        if (message.text != null) {
            container.visibility = View.GONE
            text.visibility = View.VISIBLE
            bindText(message)
        } else {
            container.visibility = View.VISIBLE
            text.visibility = View.GONE
            bindFile(message)
        }

        dateText.text = Utils.formatDate(message.sentAt)
    }

    private fun bindText(message: Message) {
        val messageText: TextView = itemView.findViewById(R.id.chatBubbleText)
        messageText.text = message.text
    }

    private fun bindFile(message: Message) {
        btn.setOnClickListener {
            btn.setImageDrawable(AppCompatResources.getDrawable(itemView.context, if (player.isPlaying) R.drawable.ic_baseline_stop_24 else R.drawable.ic_baseline_play_arrow_24))

            if (player.isPlaying) {
                stop()
            } else {
                play(message)
            }
        }

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (player != null && player.isPlaying) {
                    player.seekTo(seekBar.progress)
                }
            }
        })

        runnable = Runnable {
            seekbar.progress = player.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 1000)
        }
    }

    private fun play(message: Message) {
        try {
            if (player.isPlaying) {
                stop()
            } else if (message.file != null && message.fileReference != null) {
                val path = "${itemView.context.cacheDir}/${message.fileReference}"
                val outputFile = File(path)

                if (outputFile.isFile && outputFile.canRead()) {
                    player.setDataSource(FileInputStream(outputFile).fd)
                    player.prepare()
                    // TODO: test volume?
//                    player.setVolume(0.5f, 0.5f)
                    player.isLooping = false
                    player.start()
                } else {
                    val fos = FileOutputStream(path)
                    fos.write(message.file)
                    fos.close()
                }
            }
        } catch (e: Exception) {
            Log.w("bind_audio", "error while playing audio: ${e.message}")
        }
    }

    private fun stop() {
        btn.setImageDrawable(AppCompatResources.getDrawable(itemView.context, R.drawable.ic_baseline_play_arrow_24))
        seekbar.progress = 0
        player.stop()
        player.release()
        player.reset()
    }
}