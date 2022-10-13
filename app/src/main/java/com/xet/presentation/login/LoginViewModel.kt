package com.xet.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xet.R
import com.xet.data.Result
import com.xet.domain.usecase.user.UserUseCases
import kotlinx.coroutines.launch

class LoginViewModel(
    private val useCases: UserUseCases
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
                _loginResult.value = LoginResult(error = R.string.login_failed_error)
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        val isUserNameValid = username.isNotBlank()
        val isPasswordValid = password.isNotBlank()
        _loginForm.value = LoginFormState(
            usernameError =  if (!isUserNameValid) R.string.login_invalid_username else null,
            passwordError = if (!isPasswordValid) R.string.login_invalid_password else null,
            isDataValid = isUserNameValid && isPasswordValid
        )
    }
}