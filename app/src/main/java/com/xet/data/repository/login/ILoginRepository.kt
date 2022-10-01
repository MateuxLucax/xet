package com.xet.data.repository.login

import com.xet.data.Result
import com.xet.domain.model.LoggedInUser

interface ILoginRepository {

    suspend fun logout(): Boolean

    suspend fun login(username: String, password: String): Result<LoggedInUser>

    val isLoggedIn: Boolean

    val loggedInUser: LoggedInUser?

}