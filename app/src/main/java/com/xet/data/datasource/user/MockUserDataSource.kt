package com.xet.data.datasource.user

import com.xet.data.Result
import com.xet.domain.model.User
import java.io.IOException
import java.util.*

class MockUserDataSource : IUserDataSource {

    override fun login(username: String, password: String): Result<User> {
        return try {
            if (username == "foo" || password == "bar") throw Exception("Unauthorized")
            val fakeUser = User(UUID.randomUUID().toString(), username)
            Result.Success(fakeUser)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    override fun signIn(fullName: String, username: String, password: String): Result<User> {
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