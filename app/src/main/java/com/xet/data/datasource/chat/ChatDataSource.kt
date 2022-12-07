package com.xet.data.datasource.chat

import com.xet.data.Utils
import com.xet.data.repository.chat.model.SendMessagePayload
import com.xet.domain.model.FileType
import com.xet.domain.model.Message
import com.xet.dsd.audioRequest
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
        val to: Int,
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
                    sentAt = Utils.parseDate(message.sentAt),
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
                    file = response.body,
                    fileType = FileType.fromExtension(it.key.split(".").last()),
                    fileReference = it.key,
                    sentAt = it.value.sentAt,
                    isMine = it.value.isMine
                )
            }
        }

        return messages.asReversed()
    }

    override suspend fun sendMessage(user: String, friend: String, payload: SendMessagePayload): Message {
        var fileName: String? = null
        if (payload.file != null) {
            var request = audioRequest("put-file", payload.file, listOf("file-extension ${payload.fileType?.toExtension()}", "token ${ServiceLocator.getUserToken()}"))
            fileName = fetchDSD(request) { response ->
                if (!response.ok) throw exceptionFrom(response)

                response.parseJSON(GetFileRequest::class.java).filename
            }
        }

        val requestData = SendMessageRequest(friend.toInt(), payload.text, fileName)
        val request = jsonRequest("send-message", requestData, ServiceLocator.getUserToken())

        return fetchDSD(request) { response ->
            if (!response.ok) throw exceptionFrom(response)

            val responseData = response.parseJSON(MessageData::class.java)
            Message(
                id = responseData.id.toString(),
                text = payload.text,
                file = payload.file,
                fileType = payload.fileType,
                fileReference = fileName,
                sentAt = Utils.parseDate(responseData.sentAt.substring(0, 19), "yyyy-MM-dd'T'HH:mm:ss"), // Dunno why it returns in this format
                isMine = true
            )
        }
    }

    override suspend fun getFile(fileReference: String): ByteArray {
        val request = jsonRequest("get-file", GetFileRequest(fileReference), ServiceLocator.getUserToken())
        return fetchDSD(request) { response ->
            if (!response.ok) throw exceptionFrom(response)

            response.body
        }
    }

}
