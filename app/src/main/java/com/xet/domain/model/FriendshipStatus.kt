package com.xet.domain.model

import com.xet.R

enum class FriendshipStatus {
    FRIEND, PENDING, REFUSED;

    fun toDescription(): Int {
        return when(this) {
            FRIEND -> R.string.search_friend_description
            PENDING -> R.string.search_pending_description
            REFUSED -> R.string.search_refused_description
        }
    }

    fun toIcon(): Int {
        return when(this) {
            FRIEND -> R.drawable.ic_outline_chat_bubble_outline_24
            PENDING -> R.drawable.ic_outline_access_time_24
            REFUSED -> R.drawable.ic_outline_close_24
        }
    }
}