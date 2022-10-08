package com.xet.data.repository.user

import com.xet.data.Result
import com.xet.data.datasource.user.IUserDataSource
import com.xet.domain.model.User

class UserRepository(private val dataSource: IUserDataSource): IUserRepository {

    private var user: User? = null

    override val isLoggedIn: Boolean
        get() = user != null

    override val loggedInUser
        get() = user

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
        val result = dataSource.signIn(username, password, password);

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

    // TODO: create a method to retrieve user (remove logic from GetLoggedInUser UseCase)

    private fun setLoggedInUser(user: User) {
        this.user = user
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}