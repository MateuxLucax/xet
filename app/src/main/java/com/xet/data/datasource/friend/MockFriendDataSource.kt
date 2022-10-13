package com.xet.data.datasource.friend

import com.xet.domain.model.Contact
import com.xet.domain.model.Friend
import com.xet.domain.model.FriendshipStatus
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
                lastMessage = "Good afternoon deadbeat",
                username = "james.mary"
            ),
            Friend(
                userId = UUID.randomUUID().toString(),
                displayName = "Robert Patricia",
                status = Status.OFFLINE,
                lastMessage = "Hey! U owe me 100 bucks",
                username = "robert.patricia"
            ),
            Friend(
                userId = UUID.randomUUID().toString(),
                displayName = "John Jennifer",
                status = Status.UNDEFINED,
                lastMessage = null,
                username = "john.jennifer"
            )
        )
    }

    override suspend fun sendInvite(userFrom: String, userTo: String): Boolean {
        withContext(Dispatchers.IO) {
            Thread.sleep(1_000)
        }

        val random = Random()
        return random.nextBoolean()
    }

    override suspend fun getInvites(userId: String): List<Contact> {
        withContext(Dispatchers.IO) {
            Thread.sleep(1_000)
        }
        return listOf(
            Contact(
                userId = UUID.randomUUID().toString(),
                displayName = "James Mary",
                friendshipStatus = FriendshipStatus.PENDING,
                username = "james.mary"
            ),
            Contact(
                userId = UUID.randomUUID().toString(),
                displayName = "Robert Patricia",
                friendshipStatus = FriendshipStatus.PENDING,
                username = "robert.patricia"
            ),
            Contact(
                userId = UUID.randomUUID().toString(),
                displayName = "John Jennifer",
                friendshipStatus = FriendshipStatus.PENDING,
                username = "john.jennifer"
            ),
            Contact(
                userId = UUID.randomUUID().toString(),
                displayName = "Alfred Batman",
                friendshipStatus = FriendshipStatus.PENDING,
                username = "alfred.batman"
            )
        )
    }

    override suspend fun updateInvite(
        userFrom: String,
        userTo: String,
        accepted: Boolean
    ): Boolean {
        withContext(Dispatchers.IO) {
            Thread.sleep(1_000)
        }

        val random = Random()
        return random.nextBoolean()
    }
}