package com.xet.data.datasource.friend

import com.xet.domain.model.Friend
import com.xet.domain.model.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class MockFriendDataSource: IFriendDataSource {

    override suspend fun getFriends(userId: String): List<Friend> {
        withContext(Dispatchers.IO) {
            Thread.sleep(4_000)
        }

        return listOf(
            Friend(
                userId = UUID.randomUUID().toString(),
                displayName = "James Mary",
                status = Status.ONLINE,
                lastMessage = "Good afternoon deadbeat"
            ),
            Friend(
                userId = UUID.randomUUID().toString(),
                displayName = "Robert Patricia",
                status = Status.OFFLINE,
                lastMessage = "Hey! U owe me 100 bucks"
            ),
            Friend(
                userId = UUID.randomUUID().toString(),
                displayName = "John Jennifer",
                status = Status.UNDEFINED,
                lastMessage = null,
            )
        )
    }

    override suspend fun sendInvite(userFrom: String, userTo: String): Boolean {
        withContext(Dispatchers.IO) {
            Thread.sleep(1_000)
        }

        return true
    }
}