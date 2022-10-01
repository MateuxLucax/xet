package com.xet.data.datasource.login

import com.xet.data.Result
import com.xet.domain.model.LoggedInUser
import java.io.IOException
import java.util.*

class MockLoginDataSource : ILoginDataSource {

    override fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            if (username != "foo" || password != "bar") throw Exception("Unauthorized")
            val fakeUser = LoggedInUser(UUID.randomUUID().toString(), username)
            Result.Success(fakeUser)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    override fun logout(): Boolean {
        return true
    }
}