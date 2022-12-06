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
import com.xet.domain.model.Message
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class BindAudio(
    private val message: Message,
    private val itemView: View
) {

    private val text: TextView = itemView.findViewById(R.id.chatBubbleText)
    private val container: LinearLayout = itemView.findViewById(R.id.chatBubbleAudioContainer)
    private val btn: ImageButton = itemView.findViewById(R.id.chatMessageBtn)
    private val seekbar: SeekBar = itemView.findViewById(R.id.audioSeekbar)
    private val player = MediaPlayer()
    private lateinit var runnable: Runnable

    fun bind() {
        text.visibility = View.GONE
        container.visibility = View.VISIBLE

        btn.setOnClickListener {
            btn.setImageDrawable(AppCompatResources.getDrawable(itemView.context, if (player.isPlaying) R.drawable.ic_baseline_stop_24 else R.drawable.ic_baseline_play_arrow_24))

            if (player.isPlaying) {
                stop()
            } else {
                play()
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

    private fun play() {
        try {
            if (player.isPlaying) {
                stop()
            } else if (message.file != null && message.fileReference != null) {
                val path = "${itemView.context.cacheDir}/${message.fileReference}"
                val outputFile = File(path)

                if (outputFile.isFile && outputFile.canRead()) {
                    player.setDataSource(FileInputStream(outputFile).fd)
                    player.prepare()
                    player.setVolume(0.5f, 0.5f)
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