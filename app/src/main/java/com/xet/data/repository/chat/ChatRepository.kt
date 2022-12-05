package com.xet.data.repository.chat

import com.xet.data.Result
import com.xet.data.datasource.chat.IChatDataSource
import com.xet.domain.model.Message

class ChatRepository(
    private val dataSource: IChatDataSource
): IChatRepository {

    override suspend fun getMessages(
        user: String,
        friend: String,
        offset: Number,
        limit: Number
    ): Result<List<Message>> {
        return try {
            val result =  dataSource.getMessages(user, friend, offset, limit)

            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun sendMessage(
        user: String,
        friend: String,
        message: Message
    ): Result<Message> {
        TODO("Not yet implemented")
    }

}