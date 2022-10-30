package com.xet.domain.model

class LoggedUser(
    override val userId: String,
    override val displayName: String,
    override val username: String,
    val token: String,
    val password: String
): User(userId, displayName, username) {

    fun updated(newFullname: String, newPassword: String): LoggedUser {
        return LoggedUser(userId, newFullname, username, token, newPassword)
    }
}