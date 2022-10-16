package com.xet.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xet.R
import com.xet.domain.model.User
import com.xet.domain.usecase.chat.ChatUseCases
import com.xet.domain.usecase.user.UserUseCases
import kotlinx.coroutines.launch
import com.xet.data.Result
import com.xet.presentation.friends.FriendsResult

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

    private val _messagesResult = MutableLiveData<MessagesResult>()
    val messagesResult: LiveData<MessagesResult> = _messagesResult

    fun loadMessages() {
        viewModelScope.launch {
            val result = chatUseCases.getMessages(user.userId, friend.userId, offset, limit)

            if (result is Result.Success) {
                if (result.data.isEmpty()) {
                    _messagesResult.value = MessagesResult(empty = R.string.chat_no_messages)
                } else {
                    _messagesResult.value = MessagesResult(success = result.data)
                }
            } else {
                _messagesResult.value = MessagesResult(empty = R.string.chat_error)
            }
        }
    }



}