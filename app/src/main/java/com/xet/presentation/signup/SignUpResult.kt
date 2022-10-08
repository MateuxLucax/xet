package com.xet.presentation.signup

import com.xet.domain.model.User

data class SignUpResult(
    val success: User? = null,
    val error: Int? = null
)