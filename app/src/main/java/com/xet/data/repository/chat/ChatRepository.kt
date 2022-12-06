package com.xet.data.repository.chat

import com.xet.data.Result
import com.xet.data.datasource.chat.IChatDataSource
import com.xet.data.repository.chat.model.SendMessagePayload
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
        payload: SendMessagePayload
    ): Result<Message> {
        return try {
            val result = dataSource.sendMessage(user, friend, payload)

            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getFile(fileReference: String): Result<ByteArray> {
        return try {
            val result = dataSource.getFile(fileReference)

            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}