package com.xet.domain.model

class LoggedUser(
    userId: String,
    displayName: String,
    username: String,
    val token: String
): User(userId, displayName, username) {
}