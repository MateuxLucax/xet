package com.xet.domain.model

data class Friend(
    override val userId: String,
    override val displayName: String,
    override val username: String,
    var status: Status?,
): User(userId, displayName, username)
