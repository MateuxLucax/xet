package com.xet.domain.model

class LoggedUser(
    userId: String,
    displayName: String,
    username: String,
    val token: String,
    val password: String
): User(userId, displayName, username) {

    fun updated(newFullname: String, newPassword: String): LoggedUser {
        return LoggedUser(userId, newFullname, username, token, newPassword)
    }
}