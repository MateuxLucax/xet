package com.xet.presentation.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.xet.R
import com.xet.data.Result
import com.xet.data.Utils
import com.xet.data.repository.chat.model.SendMessagePayload
import com.xet.domain.model.FileType
import com.xet.domain.model.Message
import com.xet.domain.model.User
import com.xet.domain.usecase.chat.ChatUseCases
import com.xet.domain.usecase.user.UserUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatUseCases: ChatUseCases,
    private val userUseCases: UserUseCases,
): ViewModel() {

    private lateinit var friend: User
    private lateinit var user: User
    private var limit = 20
    private lateinit var scope: Job

    fun initialize(friend: User) {
        this.friend = friend
        val result = userUseCases.loggedInUser()

        if (result is Result.Success) {
            user = result.data
        }
    }

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    private val _messagesResult = MutableLiveData<MessagesResult>()
    val messagesResult: LiveData<MessagesResult> = _messagesResult

    fun loadMessages(offset: Number) {
       scope =  viewModelScope.launch {
            val result = chatUseCases.getMessages(user.userId, friend.userId, offset, limit)
            if (result is Result.Success) {
                if (result.data.isEmpty()) {
                    _messagesResult.value = MessagesResult(empty = R.string.chat_no_messages)
                } else {
                    _messages.value = _messages.value?.plus(result.data) ?: result.data
                }
            } else {
                _messagesResult.value = MessagesResult(empty = R.string.chat_error)
            }
        }
    }

    fun sendMessage(payload: SendMessagePayload) {
        scope =  viewModelScope.launch {
            val result = chatUseCases.sendMessageUseCase(user.userId, friend.userId, payload)
            if (result is Result.Success) {
                _messages.value = _messages.value?.plus(result.data) ?: listOf(result.data)
                _messagesResult.value = MessagesResult()
            } else {
                _messagesResult.value = MessagesResult(empty = R.string.chat_send_error)
            }
        }
    }

    fun messageHandler(m: String) {
        try {
            JsonParser.parseString(m)?.asJsonObject?.let { json ->
                if (json["type"].asJsonPrimitive.asString == "chat-message-received") {
                    val msg = Gson().fromJson(m, MessageData::class.java)

                    if (msg.fileReference != null) {
                        scope =  viewModelScope.launch {
                            val result = chatUseCases.getFileUseCase(msg.fileReference)
                            if (result is Result.Success) {
                                val message = Message(
                                    id = msg.id.toString(),
                                    file = result.data,
                                    fileType = FileType.fromExtension(msg.fileReference.split(".").last()),
                                    fileReference = msg.fileReference,
                                    sentAt = Utils.parseDate(msg.sentAt, "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"),
                                    isMine = false
                                )

                                _messages.value = _messages.value?.plus(message) ?: listOf(message)
                            }
                        }
                    } else {
                        val message = Message(
                            id = msg.id.toString(),
                            isMine = false,
                            sentAt = Utils.parseDate(msg.sentAt, "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"),
                            text = msg.textContents
                        )

                        scope =  viewModelScope.launch {
                            _messages.value = _messages.value?.plus(message) ?: listOf(message)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("chat_view_model", "Exception when parsing JSON message: $e")
        }
    }

    private data class MessageData(
        val type: String,
        val id: Int,
        val from: Int,
        val sentAt: String,
        val textContents: String?,
        val fileReference: String?
    )

    fun getFriend(): User {
        return this.friend
    }

    fun getUser(): User {
        return this.user
    }

    fun getScope(): Job {
        return this.scope
    }
}