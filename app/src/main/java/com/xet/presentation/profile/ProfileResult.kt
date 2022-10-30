package com.xet.presentation.profile

import com.xet.domain.model.LoggedUser

data class ProfileResult(
    val success: LoggedUser? = null,
    val error: Int? = null
)