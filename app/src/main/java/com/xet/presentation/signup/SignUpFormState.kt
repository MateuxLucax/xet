package com.xet.presentation.signup

data class SignUpFormState(
    val fullNameError: Int? = null,
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)