package com.xet.data.repository.user

import com.xet.data.Result
import com.xet.domain.model.LoggedUser
import com.xet.domain.model.User

interface IUserRepository {

    suspend fun logout(): Boolean

    suspend fun login(username: String, password: String): Result<LoggedUser>

    suspend fun signUp(fullName: String, username: String, password: String): Result<User>

    val isLoggedIn: Boolean

    fun getLoggedInUser(): Result<LoggedUser>

    suspend fun updateProfile(fullName: String, password: String): Result<Boolean>

}