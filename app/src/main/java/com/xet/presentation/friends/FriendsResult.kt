package com.xet.presentation.friends

import com.xet.domain.model.Friend

data class FriendsResult(
    val success: List<Friend>? = null,
    val empty: Int? = null,
    val error: Int? = null
)