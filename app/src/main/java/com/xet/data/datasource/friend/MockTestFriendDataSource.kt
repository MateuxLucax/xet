package com.xet.data.datasource.friend

import com.xet.domain.model.*
import com.xet.dsd.ErrCode
import com.xet.dsd.exceptionFrom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class MockTestFriendDataSource: IFriendDataSource {

    override suspend fun getFriends(userToken: String): List<Friend> {
        if (userToken.isEmpty()) throw exceptionFrom(ErrCode.INCORRECT_CREDENTIALS)
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
        withContext(Dispatchers.IO) {
            Thread.sleep(1_000)
        }

        val random = Random()
        return random.nextBoolean()
    }

    override suspend fun getInvites(user: LoggedUser): List<Contact> {
        withContext(Dispatchers.IO) {
            Thread.sleep(1_000)
        }
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
        withContext(Dispatchers.IO) {
            Thread.sleep(1_000)
        }

        val random = Random()
        return random.nextBoolean()
    }
}