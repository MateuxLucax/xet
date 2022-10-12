package com.xet.data.datasource.contact

import com.xet.data.Result
import com.xet.domain.model.Contact
import com.xet.domain.model.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class MockContactDataSource: IContactDataSource {

    override suspend fun getContacts(userId: String): Result<List<Contact>> {
        withContext(Dispatchers.IO) {
            Thread.sleep(4_000)
        }
        return Result.Success(
            listOf(
                Contact(
                    userId = UUID.randomUUID().toString(),
                    displayName = "James Mary",
                    status = Status.ONLINE,
                    lastMessage = "Good afternoon deadbeat"
                ),
                Contact(
                    userId = UUID.randomUUID().toString(),
                    displayName = "Robert Patricia",
                    status = Status.OFFLINE,
                    lastMessage = "Hey! U owe me 100 bucks"
                ),
                Contact(
                    userId = UUID.randomUUID().toString(),
                    displayName = "John Jennifer",
                    status = Status.UNDEFINED,
                    lastMessage = "Im gonna beat U deadbeat"
                )
            )
        )
    }

}