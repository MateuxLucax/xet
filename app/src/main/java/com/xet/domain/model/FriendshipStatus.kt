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

    // User is a friend :D
    IS_FRIEND;

    fun toDescription(): Int {
        return when(this) {
            IS_FRIEND -> R.string.search_friend_description
            SENT_FRIEND_REQUEST -> R.string.sent_friend_request
            RECEIVED_FRIEND_REQUEST -> R.string.received_friend_request
            NO_FRIEND_REQUEST -> R.string.search_send_icon
        }
    }

    fun toIcon(): Int {
        return when(this) {
            IS_FRIEND -> R.drawable.ic_outline_chat_bubble_outline_24
            RECEIVED_FRIEND_REQUEST -> R.drawable.ic_outline_access_time_24
            SENT_FRIEND_REQUEST -> R.drawable.ic_outline_close_24 // TODO: icon with user and question mark
            NO_FRIEND_REQUEST -> R.drawable.ic_baseline_person_add_24
        }
    }
}