package com.xet.domain.usecase.login

data class LoginUseCases(
    val doLogin: DoLogin,
    val doLogout: DoLogout,
    val loggedInUser: GetLoggedInUser
)