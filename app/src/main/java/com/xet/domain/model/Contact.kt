package com.xet.domain.model

data class Contact(
    val userId: String,
    val displayName: String,
    val status: Status,
    val lastMessage: String?
)