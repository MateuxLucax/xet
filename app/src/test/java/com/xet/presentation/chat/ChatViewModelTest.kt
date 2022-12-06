package com.xet.presentation.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.xet.R
import com.xet.data.datasource.chat.MockTestChatDataSource
import com.xet.data.datasource.user.MockTestUserDataSource
import com.xet.data.datasource.user.MockUserDataSource
import com.xet.data.repository.chat.ChatRepository
import com.xet.data.repository.user.UserRepository
import com.xet.domain.model.User
import com.xet.domain.usecase.chat.ChatUseCases
import com.xet.domain.usecase.chat.GetFileUseCase
import com.xet.domain.usecase.chat.GetMessagesUseCase
import com.xet.domain.usecase.chat.SendMessageUseCase
import com.xet.domain.usecase.user.*
import com.xet.utils.MainCoroutineRule
import junit.framework.TestSuite
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest: TestSuite() {
    lateinit var viewModel: ChatViewModel
    lateinit var chatUseCases: ChatUseCases
    lateinit var userUseCases: UserUseCases

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() = runBlocking {
        val userRepository = UserRepository(MockTestUserDataSource())
        val chatRepository = ChatRepository(MockTestChatDataSource())

        chatUseCases = ChatUseCases(
            getMessages =  GetMessagesUseCase(chatRepository),
            sendMessageUseCase = SendMessageUseCase(chatRepository),
            getFileUseCase = GetFileUseCase(chatRepository)
        )

        userUseCases = UserUseCases(
            doLogin = DoLogin(userRepository),
            doLogout = DoLogout(userRepository),
            loggedInUser = GetLoggedInUser(userRepository),
            doSignUp = DoSignUp(userRepository),
            isLoggedInUser = IsUserLoggedIn(userRepository),
            doUpdateProfile = DoUpdateProfile(userRepository)
        )

        userUseCases.doLogin.invoke("me", "thatisme")
        viewModel = ChatViewModel(chatUseCases, userUseCases)
    }

    @Test
    fun shouldInitializeLoadUserAndFriendData() = runBlocking {
        val friend = User(UUID.randomUUID().toString(), "Dude", "dude")
        viewModel.initialize(friend)

        assertEquals(viewModel.getFriend().username, "dude")
        assertEquals(viewModel.getUser().username, "me")
    }

    @Test
    fun shouldReturnErrorAndUpdateViewToUser() = runTest(UnconfinedTestDispatcher()) {
        val friend = User("unknownid", "Unknown", "unknown")
        viewModel.initialize(friend)

        viewModel.loadMessages(0)
        viewModel.getScope().join()

        assertEquals(viewModel.messagesResult.value?.empty, R.string.chat_error)
    }

    @Test
    fun shouldReturnNoMessagesYet() = runTest(UnconfinedTestDispatcher()) {
        val friend = User("newfriend", "Dude", "dude")
        viewModel.initialize(friend)

        viewModel.loadMessages(0)
        viewModel.getScope().join()

        assertEquals(viewModel.messagesResult.value?.empty, R.string.chat_no_messages)
    }

    @Test
    fun shouldReturnMessages() = runTest(UnconfinedTestDispatcher()) {
        val friend = User(UUID.randomUUID().toString(), "Dude", "dude")
        viewModel.initialize(friend)

        viewModel.loadMessages(0)
        viewModel.getScope().join()

        val messages = viewModel.messages.value
        messages?.isNotEmpty()?.let { assertTrue(it) }
    }
}