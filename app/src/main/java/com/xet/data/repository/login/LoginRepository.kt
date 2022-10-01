package com.xet.data.repository.login

import com.xet.data.Result
import com.xet.data.datasource.login.ILoginDataSource
import com.xet.domain.model.LoggedInUser

class LoginRepository(private val dataSource: ILoginDataSource): ILoginRepository {

    private var user: LoggedInUser? = null

    override val isLoggedIn: Boolean
        get() = user != null

    override val loggedInUser
        get() = user

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    override suspend fun login(username: String, password: String): Result<LoggedInUser> {
        val result = dataSource.login(username, password)

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

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}