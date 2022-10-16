package com.xet.data.datasource.chat

import com.xet.domain.model.Message

class MockChatDataSource: IChatDataSource {

    override suspend fun getMessages(
        user: String,
        friend: String,
        offset: Number,
        limit: Number
    ): List<Message> {
        TODO("Not yet implemented")
    }

}