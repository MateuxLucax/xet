package com.xet.presentation.profile

data class ProfileFormState(
    val passwordError: Int? = null,
    val fullNameError: Int? = null,
    val isDataValid: Boolean = false
)