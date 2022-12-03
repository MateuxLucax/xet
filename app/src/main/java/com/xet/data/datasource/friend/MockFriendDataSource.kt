package com.xet.data.datasource.friend

import com.xet.domain.model.*
import java.util.*

class MockFriendDataSource: IFriendDataSource {

    override suspend fun getFriends(userToken: String): List<Friend> {
        return listOf(
            Friend(
                userId = UUID.randomUUID().toString(),
                displayName = "James Mary",
                status = Status.ONLINE,
                username = "james.mary"
            ),
            Friend(
                userId = UUID.randomUUID().toString(),
                displayName = "Robert Patricia",
                status = Status.OFFLINE,
                username = "robert.patricia"
            ),
            Friend(
                userId = UUID.randomUUID().toString(),
                displayName = "John Jennifer",
                status = Status.OFFLINE,
                username = "john.jennifer"
            )
        )
    }

    override suspend fun sendInvite(tokenUserFrom: String, userTo: String): Boolean {
        val random = Random()
        return random.nextBoolean()
    }

    override suspend fun getInvites(user: LoggedUser): List<Contact> {
        return listOf(
            Contact(
                userId = UUID.randomUUID().toString(),
                displayName = "James Mary",
                friendshipStatus = FriendshipStatus.NO_FRIEND_REQUEST,
                username = "james.mary"
            ),
            Contact(
                userId = UUID.randomUUID().toString(),
                displayName = "Robert Patricia",
                friendshipStatus = FriendshipStatus.SENT_FRIEND_REQUEST,
                username = "robert.patricia"
            ),
            Contact(
                userId = UUID.randomUUID().toString(),
                displayName = "John Jennifer",
                friendshipStatus = FriendshipStatus.RECEIVED_FRIEND_REQUEST,
                username = "john.jennifer"
            ),
            Contact(
                userId = UUID.randomUUID().toString(),
                displayName = "Alfred Batman",
                friendshipStatus = FriendshipStatus.IS_FRIEND,
                username = "alfred.batman"
            )
        )
    }

    override suspend fun updateInvite(
        userFrom: String,
        userTo: String,
        accepted: Boolean
    ): Boolean {
        val random = Random()
        return random.nextBoolean()
    }
}