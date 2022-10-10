package com.xet.data.repository.user

import com.xet.data.Result
import com.xet.data.datasource.user.IUserDataSource
import com.xet.domain.model.User

class UserRepository(private val dataSource: IUserDataSource): IUserRepository {

    private var user: User? = null

    override val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    override suspend fun login(username: String, password: String): Result<User> {
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    override suspend fun signUp(fullName: String, username: String, password: String): Result<User> {
        val result = dataSource.signIn(fullName, username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    override suspend fun logout(): Boolean {
        if (dataSource.logout()) {
            user = null
            return true
        }

        return false
    }


    override fun getLoggedInUser(): Result<User> {
        val user = this.user
        return if (user != null) {
            Result.Success(user)
        } else {
            Result.Error(Exception("User not set"));
        }
    }

    private fun setLoggedInUser(user: User) {
        this.user = user
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}