package com.xet.data.datasource.search

import com.xet.domain.model.Contact
import com.xet.domain.model.FriendshipStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class MockSearchDataSource: ISearchDataSource {

    override suspend fun search(
        token: String,
        query: String,
        page: Int,
    ): List<Contact> {
        withContext(Dispatchers.IO) {
            Thread.sleep(2_000)
        }
        return if (query == "test") listOf(
            Contact(
                userId = UUID.randomUUID().toString(),
                displayName = "John John",
                friendshipStatus = FriendshipStatus.IS_FRIEND,
                username = "john.john"
            ),
            Contact(
                userId = UUID.randomUUID().toString(),
                displayName = "Robert Ala",
                friendshipStatus = FriendshipStatus.NO_FRIEND_REQUEST,
                username = "robert.ala"
            )
        ) else listOf(
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
                friendshipStatus = FriendshipStatus.IS_FRIEND,
                username = "john.jennifer"
            ),
            Contact(
                userId = UUID.randomUUID().toString(),
                displayName = "Alfred Batman",
                friendshipStatus = FriendshipStatus.NO_FRIEND_REQUEST,
                username = "alfred.batman"
            )
        )
    }

}