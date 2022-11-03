package com.xet.data.repository.user

import com.xet.data.Result
import com.xet.data.datasource.user.IUserDataSource
import com.xet.domain.model.LoggedUser
import com.xet.domain.model.User

class UserRepository(private val dataSource: IUserDataSource): IUserRepository {

    private var user: LoggedUser? = null

    override val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    override suspend fun login(username: String, password: String): Result<LoggedUser> {
        return try {
            val result = dataSource.signIn(username, password)
            setLoggedInUser(result)
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun signUp(fullName: String, username: String, password: String): Result<User> {
        return try {
            val result = dataSource.signUp(fullName, username, password)
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun logout(): Boolean {
        if (user != null) {
            if (dataSource.logout(user!!.token)) {
                user = null
                return true
            }
        }
        return false
    }


    override fun getLoggedInUser(): Result<LoggedUser> {
        val user = this.user
        return if (user != null) {
            Result.Success(user)
        } else {
            Result.Error(Exception("User not set"));
        }
    }

    override suspend fun updateProfile(
        fullName: String,
        password: String
    ): Result<Boolean> {
        return try {
            val token = user?.token
            if (token != null) {
                val ok = dataSource.updateProfile(token, fullName, password)
                if (ok) {
                    user = user?.updated(fullName, password)
                }
                Result.Success(ok)
            } else {
                Result.Error(Exception("Undefined user token"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private fun setLoggedInUser(user: LoggedUser) {
        this.user = user
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}