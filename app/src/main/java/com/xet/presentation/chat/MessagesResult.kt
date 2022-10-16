package com.xet.presentation.chat

import com.xet.domain.model.Message

data class MessagesResult(
    val success: List<Message>? = null,
    val empty: Int? = null,
    val error: Int? = null
)