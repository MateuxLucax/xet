package com.xet.domain.usecase.chat

import com.xet.data.Result
import com.xet.data.repository.chat.IChatRepository
import com.xet.domain.model.Message

class GetMessagesUseCase(
    private val repository: IChatRepository
) {

    suspend operator fun invoke(
        user: String,
        friend: String,
        offset: Number,
        limit: Number
    ): Result<List<Message>> {
        return repository.getMessages(user, friend, offset, limit)
    }

}