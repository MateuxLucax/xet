package com.xet.data.datasource.user

import com.xet.data.datasource.user.UserDataSource
import com.xet.dsd.ErrCode
import com.xet.dsd.ErrCodeException
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import junit.framework.TestSuite
import kotlinx.coroutines.runBlocking
import org.junit.Test

// expects the server database to be empty
// I don't know whether we should ensure this in the test somehow
// or just leave it as part of a hypothetical testing environment that we don't have

// we could mock to control that but then it wouldn't be an integration test

// the problem is that you'd need to manually clean up the server database after the test
// otherwise first it passes then it fails

class CreateUserTest : TestSuite() {

    @Test
    fun createUserSuccessfullyThenDisallowDuplicateUsername () {
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