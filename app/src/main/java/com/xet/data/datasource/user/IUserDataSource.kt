package com.xet.data.datasource.user

import com.xet.data.Result
import com.xet.domain.model.User

interface IUserDataSource {

    fun login(username: String, password: String): Result<User>

    fun signIn(fullName: String, username: String, password: String): Result<User>

    fun logout(): Boolean

}