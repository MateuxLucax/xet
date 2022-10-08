package com.xet.domain.usecase.user

data class LoginUseCases(
    val doLogin: DoLogin,
    val doLogout: DoLogout,
    val loggedInUser: GetLoggedInUser,
    val doSignUp: DoSignUp
)