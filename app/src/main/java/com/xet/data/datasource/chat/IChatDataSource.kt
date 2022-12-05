package com.xet.data.datasource.chat

import com.xet.data.repository.chat.model.SendMessagePayload
import com.xet.domain.model.Message

interface IChatDataSource {

    @Throws(Exception::class)
    suspend fun getMessages(
        user: String,
        friend: String,
        offset: Number,
        limit: Number
    ): List<Message>

    suspend fun sendMessage(
        user: String,
        friend: String,
        payload: SendMessagePayload
    ): Message

}