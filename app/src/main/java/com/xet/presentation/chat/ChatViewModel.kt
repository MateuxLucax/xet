package com.xet.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xet.domain.model.User
import com.xet.domain.usecase.chat.ChatUseCases
import com.xet.domain.usecase.user.UserUseCases
import kotlinx.coroutines.launch
import com.xet.data.Result

class ChatViewModel(
    private val chatUseCases: ChatUseCases,
    private val userUseCases: UserUseCases,
): ViewModel() {

    private lateinit var friend: User
    private lateinit var user: User
    private var offset = 0
    private var limit = 20

    fun initialize(friend: User) {
        this.friend = friend
        val result = userUseCases.loggedInUser()

        if (result is Result.Success) {
            user = result.data
        }
    }

    fun loadMessages() {
        viewModelScope.launch {
            val result = chatUseCases.getMessages(user.userId, friend.userId, offset, limit)

            if (result is Result.Success) {
                
            }
        }
    }

}