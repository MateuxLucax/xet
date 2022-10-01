package com.xet.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xet.R
import com.xet.data.Result
import com.xet.domain.usecase.login.LoginUseCases
import kotlinx.coroutines.launch

class LoginViewModel(
    private val useCases: LoginUseCases
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = useCases.doLogin(username, password)

            if (result is Result.Success) {
                _loginResult.value = LoginResult(success = result.data)
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        val userNameValid = isUserNameValid(username)
        val passwordValid = isPasswordValid(password)
        if (!userNameValid && !passwordValid) {
            _loginForm.value = LoginFormState(
                usernameError = R.string.invalid_username,
                passwordError = R.string.invalid_password
            )
        } else if (!userNameValid) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!passwordValid) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 2
    }
}