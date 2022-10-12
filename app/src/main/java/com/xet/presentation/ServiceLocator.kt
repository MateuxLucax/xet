package com.xet.presentation

import com.xet.data.datasource.friend.IFriendDataSource
import com.xet.data.datasource.friend.MockFriendDataSource
import com.xet.data.datasource.search.ISearchDataSource
import com.xet.data.datasource.search.MockSearchDataSource
import com.xet.data.datasource.user.IUserDataSource
import com.xet.data.datasource.user.MockUserDataSource
import com.xet.data.repository.friend.FriendRepository
import com.xet.data.repository.friend.IFriendRepository
import com.xet.data.repository.search.ISearchRepository
import com.xet.data.repository.search.SearchRepository
import com.xet.data.repository.user.IUserRepository
import com.xet.data.repository.user.UserRepository
import com.xet.domain.usecase.friend.DoSendInvite
import com.xet.domain.usecase.friend.FriendUseCases
import com.xet.domain.usecase.friend.GetFriends
import com.xet.domain.usecase.search.GetUsers
import com.xet.domain.usecase.search.SearchUseCases
import com.xet.domain.usecase.user.*
import com.xet.presentation.friends.FriendsViewModel
import com.xet.presentation.home.HomeViewModel
import com.xet.presentation.login.LoginViewModel
import com.xet.presentation.search.SearchViewModel
import com.xet.presentation.signup.SignUpViewModel

object ServiceLocator {

    private val loginDataSource: IUserDataSource = MockUserDataSource()
    private val loginRepository: IUserRepository = UserRepository(loginDataSource)

    private val contactDataSource: IFriendDataSource = MockFriendDataSource()
    private val friendRepository: IFriendRepository = FriendRepository(contactDataSource)

    private val searchDataSource: ISearchDataSource = MockSearchDataSource()
    private val searchRepository: ISearchRepository = SearchRepository(searchDataSource)

    private val loginUseCases = LoginUseCases(
        doLogin = DoLogin(loginRepository),
        doLogout = DoLogout(loginRepository),
        loggedInUser = GetLoggedInUser(loginRepository),
        doSignUp = DoSignUp(loginRepository),
        isLoggedInUser = IsUserLoggedIn(loginRepository)
    )

    private val friendUseCases = FriendUseCases(
        getFriends = GetFriends(friendRepository),
        sendInvite = DoSendInvite(friendRepository)
    )

    private val searchUseCases = SearchUseCases(
        getUsers = GetUsers(searchRepository)
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

    fun getContactsViewModel(): FriendsViewModel {
        return FriendsViewModel(friendUseCases)
    }

    fun getSearchViewModel(): SearchViewModel {
        return SearchViewModel(searchUseCases)
    }

}
