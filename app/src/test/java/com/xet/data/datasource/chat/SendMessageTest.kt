package com.xet.data.datasource.chat

import com.xet.data.datasource.user.UserDataSource
import com.xet.dsd.ErrCode
import com.xet.dsd.ErrCodeException
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import junit.framework.TestSuite
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SendMessageTest : TestSuite() {

    @Test
    fun shouldReturnErrorWhenUserNotAuthenticated () {
        val ds = UserDataSource()
        runBlocking {
            try {
                val user = ds.signUp("administrator", "admin", "123")
                assertEquals("administrator", user.displayName)
                assertEquals("admin", user.username)
            } catch (e : Exception) { fail() }
        }
        runBlocking {
            try {
                ds.signUp("another", "admin", "123")
                // exception should be thrown at this point
                fail()
            } catch (e : Exception) {
                if (e is ErrCodeException) {
                    assertEquals(ErrCode.USERNAME_IN_USE, e.code)
                } else fail()
            }
        }
    }
}