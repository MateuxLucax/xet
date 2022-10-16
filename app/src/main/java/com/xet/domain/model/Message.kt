package com.xet.domain.model

data class Message(
    val id: String,
    val message: String,
    val sentAt: String,
    val isMine: Boolean
)