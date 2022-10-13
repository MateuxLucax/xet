package com.xet.data.datasource.search

import com.xet.domain.model.Contact
import com.xet.domain.model.FriendshipStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class MockSearchDataSource: ISearchDataSource {

    override suspend fun search(
        userId: String,
        query: String,
        offset: Number,
        limit: Number
    ): List<Contact> {
        withContext(Dispatchers.IO) {
            Thread.sleep(2_000)
        }
        return if (query == "test") listOf(
            Contact(
                userId = UUID.randomUUID().toString(),
                displayName = "John John",
                friendshipStatus = FriendshipStatus.FRIEND,
                username = "john.john"
            ),
            Contact(
                userId = UUID.randomUUID().toString(),
                displayName = "Robert Ala",
                friendshipStatus = null,
                username = "robert.ala"
            )
        ) else listOf(
            Contact(
                userId = UUID.randomUUID().toString(),
                displayName = "James Mary",
                friendshipStatus = FriendshipStatus.REFUSED,
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
                friendshipStatus = FriendshipStatus.FRIEND,
                username = "john.jennifer"
            ),
            Contact(
                userId = UUID.randomUUID().toString(),
                displayName = "Alfred Batman",
                friendshipStatus = null,
                username = "alfred.batman"
            )
        )
    }

}