package com.xet.data.datasource.chat

import com.xet.data.Utils
import com.xet.data.repository.chat.model.SendMessagePayload
import com.xet.domain.model.Message
import com.xet.dsd.ErrCode
import com.xet.dsd.exceptionFrom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.util.*

class MockTestChatDataSource: IChatDataSource {

    private fun parseDate(date: String): LocalDateTime {
        return Utils.parseDate(date, "yyyy-MM-dd HH:mm:ss")
    }

    override suspend fun getMessages(
        user: String,
        friend: String,
        offset: Number,
        limit: Number
    ): List<Message> {
        withContext(Dispatchers.IO) {
            Thread.sleep(1_000)
        }

        if (friend == "unknownid") {
            throw exceptionFrom(ErrCode.INTERNAL)
        }

        if (friend == "newfriend") {
            return listOf()
        }

        return listOf(
            Message(
                id = UUID.randomUUID().toString(),
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                sentAt = parseDate("2022-02-15 13:03:02"),
                isMine = true,
            ),
            Message(
                id = UUID.randomUUID().toString(),
                text = "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                sentAt = parseDate("2022-02-15 13:13:14"),
                isMine = true,
            ),
            Message(
                id = UUID.randomUUID().toString(),
                text = "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
                sentAt = parseDate("2022-02-15 13:14:53"),
                isMine = false,
            ),
            Message(
                id = UUID.randomUUID().toString(),
                text = "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                sentAt = parseDate("2022-02-15 13:15:10"),
                isMine = false,
            ),
            Message(
                id = UUID.randomUUID().toString(),
                text = "Consequat mauris nunc congue nisi. Curabitur vitae nunc sed velit dignissim sodales ut eu sem. Leo integer malesuada nunc vel.",
                sentAt = parseDate("2022-02-15 13:18:58"),
                isMine = true,
            ),
            Message(
                id = UUID.randomUUID().toString(),
                text = "Metus dictum at tempor commodo ullamcorper. Nec ullamcorper sit amet risus nullam eget felis eget. Ut morbi tincidunt augue interdum velit euismod in.",
                sentAt = parseDate("2022-02-15 13:20:37"),
                isMine = false,
            ),
        )
    }

    override suspend fun sendMessage(user: String, friend: String, payload: SendMessagePayload): Message {
        return Message(
            id = UUID.randomUUID().toString(),
            text = "Metus dictum at tempor commodo ullamcorper. Nec ullamcorper sit amet risus nullam eget felis eget. Ut morbi tincidunt augue interdum velit euismod in.",
            sentAt = parseDate("2022-02-15 13:20:37"),
            isMine = false,
        )
    }

}