package com.xet.presentation

import com.xet.data.datasource.user.IUserDataSource
import com.xet.data.datasource.user.MockUserDataSource
import com.xet.data.repository.user.UserRepository
import com.xet.domain.usecase.user.*
import com.xet.presentation.home.HomeViewModel
import com.xet.presentation.login.LoginViewModel
import com.xet.presentation.signup.SignUpViewModel

object ServiceLocator {

    private val loginDataSource: IUserDataSource = MockUserDataSource()
    private val loginRepository: com.xet.data.repository.user.IUserRepository = UserRepository(loginDataSource)

    private val loginUseCases = LoginUseCases(
        doLogin = DoLogin(loginRepository),
        doLogout = DoLogout(loginRepository),
        loggedInUser = GetLoggedInUser(loginRepository),
        doSignUp = DoSignUp(loginRepository)
    )

    fun getLoginViewModel(): LoginViewModel {
        return LoginViewModel(loginUseCases)
    }

    fun getHomeViewModel(): HomeViewModel {
        return HomeViewModel(loginUseCases)
    }

    fun getMainViewModel(): MainActivity.MainViewModel {
        return MainActivity.MainViewModel(loginUseCases)
    }

    fun getSignUpViewModel(): SignUpViewModel {
        return SignUpViewModel(loginUseCases)
    }

}
