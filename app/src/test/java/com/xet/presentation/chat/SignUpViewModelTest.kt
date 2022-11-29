package com.xet.presentation.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.xet.data.datasource.user.MockTestUserDataSource
import com.xet.data.datasource.user.MockUserDataSource
import com.xet.data.repository.user.UserRepository
import com.xet.domain.model.User
import com.xet.domain.usecase.user.*
import com.xet.dsd.ErrCode
import com.xet.presentation.signup.SignUpViewModel
import com.xet.utils.MainCoroutineRule
import junit.framework.TestSuite
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelTest: TestSuite() {
    lateinit var viewModel: SignUpViewModel
    lateinit var userUseCases: UserUseCases

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val repository = UserRepository(MockTestUserDataSource())
        userUseCases = UserUseCases(DoLogin(repository),
                                        DoLogout(repository),
                                        GetLoggedInUser(repository),
                                        DoSignUp(repository),
                                        IsUserLoggedIn(repository),
                                        DoUpdateProfile(repository))
        viewModel = SignUpViewModel(userUseCases)
    }

    @Test
    fun shouldCreateAccount() = runTest(UnconfinedTestDispatcher()) {
        viewModel.signUp("Some Dude", "dude", "mypass123")
        viewModel.getScope().join()
        Assert.assertEquals(viewModel.signUpResult.value?.success!!.username, "dude")
    }

    @Test
    fun shouldReturnErrorWhenUsernameAlreadyExists() = runTest(UnconfinedTestDispatcher()) {
        viewModel.signUp("Admin", "admin", "admin132")
        viewModel.getScope().join()
        Assert.assertEquals(viewModel.signUpResult.value?.error, ErrCode.USERNAME_IN_USE.resource)
    }
}