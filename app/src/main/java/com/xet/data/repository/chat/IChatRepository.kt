package com.xet.data.repository.chat

import com.xet.data.Result
import com.xet.domain.model.Message

interface IChatRepository {

    suspend fun getMessages(
        user: String,
        friend: String,
        offset: Number,
        limit: Number
    ): Result<List<Message>>

}