package com.xet.presentation.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xet.R
import com.xet.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatInput = binding.chatMessageInput
        val chatButton = binding.chatMessageBtn
        val chatTitle = binding.chatHeaderTitle
        val loading = binding.searchListLoading
        val errorMessage = binding.chatErrorMessage



    }
}