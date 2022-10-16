package com.xet.presentation.chat

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.color.MaterialColors
import com.xet.databinding.ActivityChatBinding
import com.xet.domain.model.Message
import com.xet.domain.model.User
import com.xet.presentation.ServiceLocator
import com.xet.presentation.chat.components.ChatAdapter

class ChatActivity(
    private val viewModel: ChatViewModel = ServiceLocator.getChatViewModel()
) : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var friend: User
    private val messages: MutableList<Message> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadExtras()
        viewModel.initialize(friend)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatInput = binding.chatMessageInput
        val chatButton = binding.chatMessageBtn
        val chatTitle = binding.chatHeaderTitle
        val loading = binding.searchListLoading
        val errorMessage = binding.chatErrorMessage
        val header = binding.chatHeader
        val recyclerView = binding.chatRecyclerView

        chatTitle.title = friend.displayName
        chatTitle.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val adapter = ChatAdapter(messages, this)
        recyclerView.adapter = adapter

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

        val color = MaterialColors.getColor(this, com.google.android.material.R.attr.colorSecondaryVariant, Color.BLACK)
        window.navigationBarColor = color
    }

    private fun loadExtras() {
        val bundle = intent.extras
        val id = bundle?.getString(Companion.FRIEND_ID)
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

}