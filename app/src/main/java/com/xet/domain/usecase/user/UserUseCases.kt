package com.xet.domain.usecase.user

data class UserUseCases(
    val doLogin: DoLogin,
    val doLogout: DoLogout,
    val loggedInUser: GetLoggedInUser,
    val doSignUp: DoSignUp,
    val isLoggedInUser: IsUserLoggedIn,
    val doUpdateProfile: DoUpdateProfile
)