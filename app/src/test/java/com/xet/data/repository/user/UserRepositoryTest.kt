package com.xet.data.repository.user

import com.xet.data.Result
import com.xet.data.datasource.user.MockTestUserDataSource
import com.xet.dsd.ErrCode
import com.xet.dsd.ErrCodeException
import junit.framework.TestSuite
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class UserRepositoryTest: TestSuite() {
    lateinit var repository: UserRepository

    @Before
    fun setUp() {
        repository = UserRepository(MockTestUserDataSource())
    }

    @Test
    fun shouldReturnErrorWhenUserAlreadyExists() = runTest {
        val result = repository.signUp("Administrator", "admin", "xpto02539")

        if (result is Result.Error) {
            assertEquals(ErrCode.USERNAME_IN_USE.resource, (result.exception as ErrCodeException).code.resource)
        } else {
            fail()
        }
    }

    @Test
    fun shouldReturnSucessWithValidData() = runTest {
        val result = repository.signUp("Testimus", "testium", "test01205")

        if (result is Result.Success) {
            assertEquals("testium", result.data.username)
            assertTrue(result.data.userId.isNotEmpty())
        } else {
            fail()
        }
    }
}