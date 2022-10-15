package com.xet.presentation.chat

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.color.MaterialColors
import com.xet.R
import com.xet.databinding.ActivityChatBinding
import com.xet.domain.model.Friend
import com.xet.domain.model.User

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var friend: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadExtras()

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatInput = binding.chatMessageInput
        val chatButton = binding.chatMessageBtn
        val chatTitle = binding.chatHeaderTitle
        val loading = binding.searchListLoading
        val errorMessage = binding.chatErrorMessage
        val header = binding.chatHeader

        chatTitle.title = friend.displayName
        chatTitle.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

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