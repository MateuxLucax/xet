package com.xet.data.datasource.user

import com.xet.data.Result
import com.xet.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class MockUserDataSource : IUserDataSource {

    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            withContext(Dispatchers.IO) {
                Thread.sleep(2_000)
            }
            if (username == "foo" || password == "bar") throw Exception("Unauthorized")
            val fakeUser = User(UUID.randomUUID().toString(), username)
            Result.Success(fakeUser)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    override suspend fun signIn(fullName: String, username: String, password: String): Result<User> {
        withContext(Dispatchers.IO) {
            Thread.sleep(6_000)
        }
        return try {
            if (fullName == "test" && username == "foo" && password == "12345678") throw Exception("Unauthorized")
            val fakeUser = User(UUID.randomUUID().toString(), username)
            Result.Success(fakeUser)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    override fun logout(): Boolean {
        return true
    }
}