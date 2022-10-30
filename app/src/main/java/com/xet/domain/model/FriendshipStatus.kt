package com.xet.domain.model

import com.xet.R

fun friendshipStatusFrom(s: String): FriendshipStatus? {
    return FriendshipStatus.values().find{ it.name == s }
}

// Enum names must match ones defined in dsd-tf
// (otherwise friendshipStatusFrom needs to change)

enum class FriendshipStatus {
    // User is not a friend and there's no friend request involving him
    NO_FRIEND_REQUEST,

    // User is not a friend but has sent you a friend request
    SENT_FRIEND_REQUEST,

    // User is not a friend but you have sent him a friend request
    RECEIVED_FRIEND_REQUEST,

    // User is a friend :)
    IS_FRIEND;

    fun toDescription(): Int {
        return when(this) {
            IS_FRIEND -> R.string.search_friend_description
            SENT_FRIEND_REQUEST -> R.string.TODO
            RECEIVED_FRIEND_REQUEST -> R.string.TODO
            NO_FRIEND_REQUEST -> R.string.TODO
        }
    }

    fun toIcon(): Int {
        return when(this) {
            IS_FRIEND -> R.drawable.ic_outline_chat_bubble_outline_24
            RECEIVED_FRIEND_REQUEST -> R.drawable.ic_outline_access_time_24
            SENT_FRIEND_REQUEST -> R.drawable.ic_outline_close_24 // TODO
            NO_FRIEND_REQUEST -> R.drawable.ic_outline_close_24 // TODO
        }
    }
}