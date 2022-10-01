package com.xet.presentation.login

import com.xet.domain.model.LoggedInUser

data class LoginResult(
    val success: LoggedInUser? = null,
    val error: Int? = null
)