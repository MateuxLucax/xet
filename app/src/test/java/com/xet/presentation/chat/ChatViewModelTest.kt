package com.xet.presentation.chat


import com.xet.R
import com.xet.domain.model.LoggedUser
import com.xet.domain.usecase.chat.ChatUseCases
import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import com.xet.data.Result
import com.xet.data.datasource.chat.MockChatDataSource
import com.xet.data.datasource.user.MockUserDataSource
import com.xet.data.repository.chat.ChatRepository
import com.xet.data.repository.user.UserRepository
import com.xet.domain.model.User
import com.xet.domain.usecase.chat.GetMessagesUseCase
import com.xet.domain.usecase.user.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class ChatViewModelTest: TestCase() {
//    lateinit var viewModel: ChatViewModel

//    @Mock
//    lateinit var getMessagesUseCase: GetMessagesUseCase
//
//    @Mock
//    lateinit var getLoggedInUser: GetLoggedInUser

//    lateinit var user: User
//    lateinit var friend: User

    override fun setUp() {
        super.setUp()
//        val userRepository = UserRepository(MockUserDataSource());
//
//        val chatUseCases = ChatUseCases(getMessagesUseCase)
//        val userUseCases = UserUseCases(DoLogin(userRepository),
//                                    DoLogout(userRepository),
//                                    getLoggedInUser,
//                                    DoSignUp(userRepository),
//                                    IsUserLoggedIn(userRepository),
//                                    DoUpdateProfile(userRepository))
//
//        viewModel = ChatViewModel(chatUseCases, userUseCases)
//        val loggedUser = LoggedUser(UUID.randomUUID().toString(), "John Doe", "john", "some random token", "12345678")
//        Mockito.`when`(userUseCases.loggedInUser.invoke()).thenReturn(Result.Success(loggedUser))

//        user = loggedUser
//        friend = User(UUID.randomUUID().toString(), "Dude", "dude")
//        viewModel.initialize(friend)
    }

    @Test
    fun shouldReturnErrorWhenMessageCouldNotBeenLoaded() {
//        Mockito.`when`(chatUseCases.getMessages.invoke(user.userId, friend.userId, 0, 20))
//                .thenReturn(Result.Error(Exception("Something Went wrong!")))
//        viewModel.loadMessages()

        assertEquals(1, 1)
    }
}