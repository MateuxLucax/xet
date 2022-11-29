package com.xet.data.repository.search

import com.xet.data.Result
import com.xet.data.datasource.search.MockTestSearchDataSource
import com.xet.domain.model.FriendshipStatus
import junit.framework.TestSuite
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class SearchRepositoryTest: TestSuite() {
    lateinit var repository: SearchRepository

    @Before
    fun setUp() {
        repository = SearchRepository(MockTestSearchDataSource())
    }

    @Test
    fun shouldReturnAllResultsWhenSearchFieldIsEmpty() = runTest {
        val result = repository.search("", "", 1)

        if (result is Result.Success) {
            assertTrue(result.data.size == 4)
            assertEquals(result.data.get(0).friendshipStatus, FriendshipStatus.NO_FRIEND_REQUEST)
            assertEquals(result.data.get(1).friendshipStatus, FriendshipStatus.SENT_FRIEND_REQUEST)
            assertEquals(result.data.get(2).friendshipStatus, FriendshipStatus.IS_FRIEND)
        } else {
            fail()
        }
    }

    @Test
    fun shouldReturnFilteredResultsWhenSearchFieldIsNotEmpty() = runTest {
        val result = repository.search("", "test", 1)

        if (result is Result.Success) {
            assertTrue(result.data.size == 2)
        } else {
            fail()
        }
    }
}