package com.xet.presentation

import com.xet.data.datasource.chat.IChatDataSource
import com.xet.data.datasource.chat.MockChatDataSource
import com.xet.data.datasource.friend.IFriendDataSource
import com.xet.data.datasource.friend.MockFriendDataSource
import com.xet.data.datasource.search.ISearchDataSource
import com.xet.data.datasource.search.MockSearchDataSource
import com.xet.data.datasource.user.IUserDataSource
import com.xet.data.datasource.user.MockUserDataSource
import com.xet.data.repository.chat.ChatRepository
import com.xet.data.repository.chat.IChatRepository
import com.xet.data.datasource.user.UserDataSource
import com.xet.data.repository.friend.FriendRepository
import com.xet.data.repository.friend.IFriendRepository
import com.xet.data.repository.search.ISearchRepository
import com.xet.data.repository.search.SearchRepository
import com.xet.data.repository.user.IUserRepository
import com.xet.data.repository.user.UserRepository
import com.xet.domain.usecase.chat.ChatUseCases
import com.xet.domain.usecase.chat.GetMessagesUseCase
import com.xet.domain.usecase.friend.*
import com.xet.domain.usecase.search.GetUsers
import com.xet.domain.usecase.search.SearchUseCases
import com.xet.domain.usecase.user.*
import com.xet.presentation.chat.ChatViewModel
import com.xet.presentation.friends.FriendsViewModel
import com.xet.presentation.home.HomeViewModel
import com.xet.presentation.login.LoginViewModel
import com.xet.presentation.profile.ProfileViewModel
import com.xet.presentation.search.SearchViewModel
import com.xet.presentation.signup.SignUpViewModel

object ServiceLocator {

    private val userDatasource: IUserDataSource = UserDataSource()
    private val userRepository: IUserRepository = UserRepository(userDatasource)

    private val contactDataSource: IFriendDataSource = MockFriendDataSource()
    private val friendRepository: IFriendRepository = FriendRepository(contactDataSource)

    private val searchDataSource: ISearchDataSource = MockSearchDataSource()
    private val searchRepository: ISearchRepository = SearchRepository(searchDataSource)

    private val chatDataSource: IChatDataSource = MockChatDataSource()
    private val chatRepository: IChatRepository = ChatRepository(chatDataSource)

    private val userUseCases = UserUseCases(
        doLogin = DoLogin(userRepository),
        doLogout = DoLogout(userRepository),
        loggedInUser = GetLoggedInUser(userRepository),
        doSignUp = DoSignUp(userRepository),
        isLoggedInUser = IsUserLoggedIn(userRepository),
        doUpdateProfile = DoUpdateProfile(userRepository)
    )

    private val friendUseCases = FriendUseCases(
        getFriends = GetFriends(friendRepository),
        sendInvite = DoSendInvite(friendRepository),
        getInvites = GetInvites(friendRepository),
        updateInvite = DoUpdateInvite(friendRepository)
    )

    private val searchUseCases = SearchUseCases(
        getUsers = GetUsers(searchRepository)
    )

    private val chatUseCases = ChatUseCases(
        getMessages = GetMessagesUseCase(chatRepository)
    )

    fun getLoginViewModel(): LoginViewModel {
        return LoginViewModel(userUseCases)
    }

    fun getHomeViewModel(): HomeViewModel {
        return HomeViewModel(userUseCases)
    }

    fun getMainViewModel(): MainActivity.MainViewModel {
        return MainActivity.MainViewModel(userUseCases)
    }

    fun getSignUpViewModel(): SignUpViewModel {
        return SignUpViewModel(userUseCases)
    }

    fun getContactsViewModel(): FriendsViewModel {
        return FriendsViewModel(friendUseCases)
    }

    fun getSearchViewModel(): SearchViewModel {
        return SearchViewModel(searchUseCases, friendUseCases)
    }

    fun getProfileViewModel(): ProfileViewModel {
        return ProfileViewModel(userUseCases, friendUseCases)
    }

    fun getChatViewModel(): ChatViewModel {
        return ChatViewModel(chatUseCases, userUseCases)
    }

}
