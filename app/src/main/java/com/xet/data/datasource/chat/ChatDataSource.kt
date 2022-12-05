package com.xet.data.datasource.chat

import com.xet.domain.model.Message
import com.xet.dsd.exceptionFrom
import com.xet.dsd.fetchDSD
import com.xet.dsd.jsonRequest
import com.xet.presentation.ServiceLocator

class ChatDataSource: IChatDataSource {

    private data class GetMessagesRequest(
        val friendId: Long,
        val before: Number,
        val limit: Number
    )

    private data class GetFileRequest(
        val filename: String
    )

    private data class MessageData(
        val id: Int,
        val userId: Int,
        val sentAt: String,
        val textContents: String?,
        val fileReference: String?
    )

    private data class SendMessageRequest(
        val id: Int,
        val textContents: String?,
        val fileReference: String?
    )

    override suspend fun getMessages(
        user: String,
        friend: String,
        offset: Number,
        limit: Number
    ): List<Message> {
        val messagesToGetFile: MutableMap<String, Message> = mutableMapOf()
        val requestData = GetMessagesRequest(friend.toLong(), offset, limit)
        val request = jsonRequest("get-messages", requestData, ServiceLocator.getUserToken())
        val messages = fetchDSD(request) { response ->
            if (!response.ok) throw exceptionFrom(response)

            val responseData = response.parseJSON(Array<MessageData>::class.java)
            responseData.map{ message ->
                val msg = Message(
                    id = message.id.toString(),
                    text = message.textContents,
                    file = null,
                    sentAt = message.sentAt,
                    isMine = message.userId.toString() == user
                )

                if (message.fileReference != null) {
                    messagesToGetFile[message.fileReference] = msg
                }

                msg
            }.toList()
        }.toMutableList()

        messagesToGetFile.forEach {
            val request = jsonRequest("get-file", GetFileRequest(it.key), ServiceLocator.getUserToken())
            fetchDSD(request) { response ->
                if (!response.ok) throw exceptionFrom(response)

                messages[messages.indexOf(it.value)] = Message(
                    id = it.value.id,
                    text = null,
                    file = response.body,
                    sentAt = it.value.sentAt,
                    isMine = it.value.isMine
                )
            }
        }

        return messages
    }

    override suspend fun sendMessage(user: String, friend: String, message: Message): Message {
        var fileName: String? = null
        if (message.file != null) {
            var request = jsonRequest("put-file", message.file, ServiceLocator.getUserToken())
            fileName = fetchDSD(request) { response ->
                if (!response.ok) throw exceptionFrom(response)

                response.parseJSON(GetFileRequest::class.java).filename
            }
        }

        val requestData = SendMessageRequest(user.toInt(), message.text, fileName)

        val request = jsonRequest("send-message", requestData, ServiceLocator.getUserToken())
        return fetchDSD(request) { response ->
            if (!response.ok) throw exceptionFrom(response)

            val responseData = response.parseJSON(MessageData::class.java)
            Message(
                id = responseData.id.toString(),
                text = responseData.textContents,
                file = message.file,
                sentAt = responseData.sentAt,
                isMine = responseData.userId.toString() == user
            )
        }
    }

}
