package com.xet.presentation.chat

import android.Manifest.permission.ACCESS_MEDIA_LOCATION
import android.Manifest.permission.RECORD_AUDIO
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.color.MaterialColors
import com.xet.R
import com.xet.databinding.ActivityChatBinding
import com.xet.domain.model.Message
import com.xet.domain.model.User
import com.xet.presentation.ServiceLocator
import com.xet.presentation.chat.components.ChatAdapter
import java.io.IOException

const val REQUEST_AUDIO_PERMISSION_CODE = 1

class ChatActivity(
    private val viewModel: ChatViewModel = ServiceLocator.getChatViewModel()
) : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var friend: User
    private val messages: MutableList<Message> = mutableListOf()
    private var recorder: MediaRecorder? = null
    private var isRecording = false
    private var audioFile: String? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadExtras()
        viewModel.initialize(friend)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatInput = binding.chatMessageInput
        val chatAudioBtn = binding.chatAudioMessageBtn
        val chatButton = binding.chatMessageBtn
        val chatTitle = binding.chatHeaderTitle
        val loading = binding.searchListLoading
        val errorMessage = binding.chatErrorMessage
        val recyclerView = binding.chatRecyclerView

        chatTitle.title = friend.displayName
        chatTitle.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val adapter = ChatAdapter(messages, this)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false

        viewModel.messagesResult.observe(this@ChatActivity, Observer {
            val result = it ?: return@Observer

            loading.visibility = View.GONE
            if (result.empty != null) {
                errorMessage.text = getString(result.empty)
            } else if (result.success != null) {
                messages.addAll(result.success)
                adapter.notifyDataSetChanged()
            } else if (result.error != null) {
                errorMessage.text = getString(result.error)
            }
        })

        loading.visibility = View.VISIBLE
        viewModel.loadMessages()

        chatButton.setOnClickListener {
            if (isRecording) {
                stopAudioRecording()
                playAudio()
            } else {
                startAudioRecording()
            }
        }

        chatInput.afterTextChanged {
            stopAudioRecording()
            if (it.isNotEmpty()) {
                chatButton.visibility = View.VISIBLE
                chatAudioBtn.visibility = View.GONE
            } else {
                chatButton.visibility = View.GONE
                chatAudioBtn.visibility = View.VISIBLE
            }
        }

        val color = MaterialColors.getColor(this, com.google.android.material.R.attr.colorSecondaryVariant, Color.BLACK)
        window.navigationBarColor = color
    }

    private fun loadExtras() {
        val bundle = intent.extras
        val id = bundle?.getString(FRIEND_ID)
        val name = bundle?.getString(FRIEND_NAME)
        val username = bundle?.getString(FRIEND_USER_NAME)

        if (id != null && name != null && username != null) {
            friend = User(id, name, username)
        }
    }

    companion object {
        const val FRIEND_ID = "friend_id"
        const val FRIEND_NAME = "friend_name"
        const val FRIEND_USER_NAME = "friend_user_name"
    }

    private fun isAudioPermissionGranted(): Boolean {
        val storagePermission = ContextCompat.checkSelfPermission(applicationContext, ACCESS_MEDIA_LOCATION)
        val audioPermission = ContextCompat.checkSelfPermission(applicationContext, RECORD_AUDIO)
        return storagePermission == PackageManager.PERMISSION_GRANTED && audioPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this@ChatActivity,
            arrayOf(RECORD_AUDIO, ACCESS_MEDIA_LOCATION),
            REQUEST_AUDIO_PERMISSION_CODE,
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE
            && grantResults.first() == PackageManager.PERMISSION_GRANTED
            && grantResults.last() == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(applicationContext, R.string.storage_permission_granted, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, R.string.storage_permission_denied, Toast.LENGTH_LONG).show()
        }
    }

    private fun startAudioRecording() {
        if (isAudioPermissionGranted()) {
            recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(this)
            } else {
                MediaRecorder()
            }

            audioFile = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)}/xet/audio.3gp"
            recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            recorder?.setOutputFile(audioFile)

            try {
                recorder?.prepare()
            } catch (e: Exception) {
                Log.w("error while recording audio.", e)
            }

            recorder?.start()
            isRecording = true
            Log.i("event", "start_recording")
        } else {
            requestPermissions()
        }
    }

    private fun stopAudioRecording() {
        recorder?.stop()
        recorder?.release()
        isRecording = false

        Log.i("event", "stop_recording")
    }

    private fun playAudio() {
        val player = MediaPlayer()
        try {
            player.setDataSource(audioFile)
            player.prepare()
            player.start()
        } catch (e: IOException) {
            Log.e("TAG", "prepare() failed")
        }
    }
}

fun TextView.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}
