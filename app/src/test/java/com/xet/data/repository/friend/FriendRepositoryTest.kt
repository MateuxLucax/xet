package com.xet.data.repository.friend

import com.xet.data.Result
import com.xet.data.datasource.friend.MockFriendDataSource
import com.xet.data.datasource.friend.MockTestFriendDataSource
import com.xet.data.datasource.search.MockTestSearchDataSource
import com.xet.domain.model.FriendshipStatus
import com.xet.dsd.ErrCode
import com.xet.dsd.ErrCodeException
import junit.framework.TestSuite
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import java.util.UUID

class FriendRepositoryTest: TestSuite() {
    lateinit var repository: FriendRepository

    @Before
    fun setUp() {
        repository = FriendRepository(MockTestFriendDataSource())
    }

    @Test
    fun shouldReturnErrorWhenTokenIsInvalid() = runTest {
        val result = repository.getFriends("")
        if (result is Result.Error) {
            assertEquals(ErrCode.INCORRECT_CREDENTIALS.resource, (result.exception as ErrCodeException).code.resource)
        } else {
            fail()
        }
    }

    @Test
    fun shouldReturnDataWhenTokenIsValid() = runTest {
        val result = repository.getFriends(UUID.randomUUID().toString())
        if (result is Result.Success) {
            assertTrue(result.data.isNotEmpty())
            assertTrue(result.data.get(0).userId.isNotEmpty())
        } else {
            fail()
        }
    }
}