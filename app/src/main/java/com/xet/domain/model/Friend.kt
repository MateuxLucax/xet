package com.xet.domain.model

data class Friend(
    override val userId: String,
    override val displayName: String,
    override val username: String,
    val status: Status?,
    val lastMessage: String?,
): User(userId, displayName, username)
