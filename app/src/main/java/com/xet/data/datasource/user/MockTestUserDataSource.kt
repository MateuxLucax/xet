package com.xet.data.datasource.user

import com.xet.domain.model.LoggedUser
import com.xet.domain.model.User
import com.xet.dsd.ErrCode
import com.xet.dsd.exceptionFrom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class MockTestUserDataSource : IUserDataSource {

    override suspend fun signIn(username: String, password: String): LoggedUser {
        withContext(Dispatchers.IO) {
            Thread.sleep(2_000)
        }
        if (username == "foo" || password == "bar") throw Exception("Unauthorized")
        return LoggedUser(UUID.randomUUID().toString(), "John Doe", username, "some random token", "12345678")
    }

    override suspend fun signUp(fullName: String, username: String, password: String): User {
        if (username == "admin") throw exceptionFrom(ErrCode.USERNAME_IN_USE)

        if (fullName == "test" && username == "foo" && password == "12345678") throw Exception("Unauthorized")
        return User(UUID.randomUUID().toString(), fullName, username)
    }

    override suspend fun logout(token: String): Boolean {
        val random = Random()
        return random.nextBoolean()
    }

    override suspend fun updateProfile(
        token: String,
        fullName: String,
        password: String
    ): Boolean {
        withContext(Dispatchers.IO) {
            Thread.sleep(2_000)
        }

        val random = Random()
        return random.nextBoolean()
    }
}