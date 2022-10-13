package com.xet.presentation.profile

import com.xet.domain.model.User

data class ProfileResult(
    val success: User? = null,
    val error: Int? = null
)