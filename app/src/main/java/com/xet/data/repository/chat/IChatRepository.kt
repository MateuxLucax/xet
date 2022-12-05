package com.xet.data.repository.chat

import com.xet.data.Result
import com.xet.data.repository.chat.model.SendMessagePayload
import com.xet.domain.model.Message

interface IChatRepository {

    suspend fun getMessages(
        user: String,
        friend: String,
        offset: Number,
        limit: Number
    ): Result<List<Message>>

    suspend fun sendMessage(
        user: String,
        friend: String,
        payload: SendMessagePayload
    ): Result<Message>

}