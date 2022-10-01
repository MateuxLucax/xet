package com.xet.presentation

import com.xet.data.datasource.login.ILoginDataSource
import com.xet.data.datasource.login.MockLoginDataSource
import com.xet.data.repository.login.ILoginRepository
import com.xet.data.repository.login.LoginRepository
import com.xet.domain.usecase.login.DoLogin
import com.xet.domain.usecase.login.DoLogout
import com.xet.domain.usecase.login.GetLoggedInUser
import com.xet.domain.usecase.login.LoginUseCases
import com.xet.presentation.login.LoginViewModel

object ServiceLocator {

    private val loginDataSource: ILoginDataSource = MockLoginDataSource()
    private val loginRepository: ILoginRepository = LoginRepository(loginDataSource)

    fun getLoginUseCases(): LoginUseCases {
        return LoginUseCases(
            doLogin = DoLogin(loginRepository),
            doLogout = DoLogout(loginRepository),
            loggedInUser = GetLoggedInUser(loginRepository)
        )
    }

    fun getLoginViewModel(): LoginViewModel {
        return LoginViewModel(getLoginUseCases().doLogin)
    }
}
