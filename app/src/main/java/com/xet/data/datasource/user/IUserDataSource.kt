package com.xet.data.datasource.user

import com.xet.data.Result
import com.xet.domain.model.User

interface IUserDataSource {

    suspend fun login(username: String, password: String): Result<User>

    suspend fun signIn(fullName: String, username: String, password: String): Result<User>

    fun logout(): Boolean

}