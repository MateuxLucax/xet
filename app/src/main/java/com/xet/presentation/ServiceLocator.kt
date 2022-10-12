package com.xet.presentation

import com.xet.data.datasource.contact.IContactDataSource
import com.xet.data.datasource.contact.MockContactDataSource
import com.xet.data.datasource.user.IUserDataSource
import com.xet.data.datasource.user.MockUserDataSource
import com.xet.data.repository.contact.ContactRepository
import com.xet.data.repository.contact.IContactRepository
import com.xet.data.repository.user.IUserRepository
import com.xet.data.repository.user.UserRepository
import com.xet.domain.usecase.contact.ContactUseCases
import com.xet.domain.usecase.contact.GetContacts
import com.xet.domain.usecase.user.*
import com.xet.presentation.contacts.ContactsViewModel
import com.xet.presentation.home.HomeViewModel
import com.xet.presentation.login.LoginViewModel
import com.xet.presentation.signup.SignUpViewModel

object ServiceLocator {

    private val loginDataSource: IUserDataSource = MockUserDataSource()
    private val loginRepository: IUserRepository = UserRepository(loginDataSource)

    private val contactDataSource: IContactDataSource = MockContactDataSource()
    private val contactRepository: IContactRepository = ContactRepository(contactDataSource)

    private val loginUseCases = LoginUseCases(
        doLogin = DoLogin(loginRepository),
        doLogout = DoLogout(loginRepository),
        loggedInUser = GetLoggedInUser(loginRepository),
        doSignUp = DoSignUp(loginRepository),
        isLoggedInUser = IsUserLoggedIn(loginRepository)
    )

    private val contactUseCases = ContactUseCases(
        getContacts = GetContacts(contactRepository)
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

    fun getContactsViewModel(): ContactsViewModel {
        return ContactsViewModel(contactUseCases)
    }

}
