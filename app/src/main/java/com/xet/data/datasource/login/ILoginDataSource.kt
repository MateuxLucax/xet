package com.xet.data.datasource.login

import com.xet.data.Result
import com.xet.domain.model.LoggedInUser

interface ILoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser>

    fun logout(): Boolean

}