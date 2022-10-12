package com.xet.data.datasource.user

import com.xet.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class MockUserDataSource : IUserDataSource {

    override suspend fun signIn(username: String, password: String): User {
        withContext(Dispatchers.IO) {
            Thread.sleep(2_000)
        }
        if (username == "foo" || password == "bar") throw Exception("Unauthorized")
        return User(UUID.randomUUID().toString(), username)
    }

    @Throws(Exception::class)
    override suspend fun signUp(fullName: String, username: String, password: String): User {
        withContext(Dispatchers.IO) {
            Thread.sleep(6_000)
        }

        if (fullName == "test" && username == "foo" && password == "12345678") throw Exception("Unauthorized")
        return User(UUID.randomUUID().toString(), username)
    }

    override fun logout(): Boolean {
        return true
    }
}