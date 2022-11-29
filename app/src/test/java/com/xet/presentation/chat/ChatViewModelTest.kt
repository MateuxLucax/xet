package com.xet.presentation.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.xet.R
import com.xet.data.datasource.chat.MockChatDataSource
import com.xet.data.datasource.chat.MockTestChatDataSource
import com.xet.data.datasource.user.MockUserDataSource
import com.xet.data.repository.chat.ChatRepository
import com.xet.data.repository.user.UserRepository
import com.xet.domain.model.User
import com.xet.domain.usecase.chat.ChatUseCases
import com.xet.domain.usecase.chat.GetMessagesUseCase
import com.xet.domain.usecase.user.*
import com.xet.presentation.chat.ChatViewModel
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
        val userRepository = UserRepository(MockUserDataSource());
        val chatRepository = ChatRepository(MockTestChatDataSource());

        chatUseCases = ChatUseCases(GetMessagesUseCase(chatRepository))
        userUseCases = UserUseCases(DoLogin(userRepository),
            DoLogout(userRepository),
            GetLoggedInUser(userRepository),
            DoSignUp(userRepository),
            IsUserLoggedIn(userRepository),
            DoUpdateProfile(userRepository))
    }

    @Test
    fun shouldInitializeLoadUserAndFriendData() = runBlocking {
        userUseCases.doLogin.invoke("me", "thatisme")
        viewModel = ChatViewModel(chatUseCases, userUseCases)

        val friend = User(UUID.randomUUID().toString(), "Dude", "dude")
        viewModel.initialize(friend)

        assertEquals(viewModel.getFriend().username, "dude")
        assertEquals(viewModel.getUser().username, "me")
    }

    @Test
    fun shouldReturnErrorAndUpdateViewToUser() = runTest(UnconfinedTestDispatcher()) {
        userUseCases.doLogin.invoke("me", "thatisme")
        viewModel = ChatViewModel(chatUseCases, userUseCases)

        val friend = User("unknownid", "Unknown", "unknown")
        viewModel.initialize(friend)

        viewModel.loadMessages()
        viewModel.getScope().join();

        assertEquals(viewModel.messagesResult.value?.empty, R.string.chat_error)
    }

    @Test
    fun shouldReturnNoMessagesYet() = runTest(UnconfinedTestDispatcher()) {
        userUseCases.doLogin.invoke("me", "thatisme")
        viewModel = ChatViewModel(chatUseCases, userUseCases)

        val friend = User("newfriend", "Dude", "dude")
        viewModel.initialize(friend)

        viewModel.loadMessages()
        viewModel.getScope().join();

        assertEquals(viewModel.messagesResult.value?.empty, R.string.chat_no_messages)
    }

    @Test
    fun shouldReturnMessages() = runTest(UnconfinedTestDispatcher()) {
        userUseCases.doLogin.invoke("me", "thatisme")
        viewModel = ChatViewModel(chatUseCases, userUseCases)

        val friend = User(UUID.randomUUID().toString(), "Dude", "dude")
        viewModel.initialize(friend)

        viewModel.loadMessages()
        viewModel.getScope().join();

        val messages = viewModel.messagesResult.value?.success
        assertTrue(messages?.size!! > 0)
    }
}