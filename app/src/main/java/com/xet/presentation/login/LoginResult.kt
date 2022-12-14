package com.xet.presentation.login

import com.xet.domain.model.User

data class LoginResult(
    val success: User? = null,
    val error: Int? = null
)