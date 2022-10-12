package com.xet.domain.model

open class Contact(
    override val userId: String,
    override val displayName: String,
    val friendshipStatus: FriendshipStatus?
) : User (userId, displayName)