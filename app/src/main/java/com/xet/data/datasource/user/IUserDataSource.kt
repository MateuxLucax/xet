package com.xet.data.datasource.user

import com.xet.domain.model.User

interface IUserDataSource {

    @Throws(Exception::class)
    suspend fun signIn(username: String, password: String): User

    @Throws(Exception::class)
    suspend fun signUp(fullName: String, username: String, password: String): User

    fun logout(): Boolean

}