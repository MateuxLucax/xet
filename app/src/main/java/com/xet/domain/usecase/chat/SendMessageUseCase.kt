package com.xet.domain.usecase.chat

import com.xet.data.Result
import com.xet.data.repository.chat.IChatRepository
import com.xet.data.repository.chat.model.SendMessagePayload
import com.xet.domain.model.Message

class SendMessageUseCase(
    private val repository: IChatRepository
) {

    suspend operator fun invoke(user: String, friend: String, payload: SendMessagePayload): Result<Message> {
        return repository.sendMessage(user, friend, payload)
    }

}