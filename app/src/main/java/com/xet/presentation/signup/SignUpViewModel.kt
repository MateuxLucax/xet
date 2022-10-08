package com.xet.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xet.R
import com.xet.data.Result
import com.xet.domain.usecase.user.LoginUseCases
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val useCases: LoginUseCases
): ViewModel() {

    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult

    fun signUp(fullName: String, username: String,password: String) {
        viewModelScope.launch {
            val result = useCases.doSignUp(fullName, username, password)

            if (result is Result.Success) {
                _signUpResult.value = SignUpResult(success = result.data)
            } else {
                _signUpResult.value = SignUpResult(error = R.string.signup_failed_error)
            }
        }
    }

    fun loginDataChanged(fullName: String, username: String, password: String) {
        val isFullNameValid = fullName.isNotBlank()
        val isUserNameValid = username.isNotBlank()
        val isPasswordValid = password.length > 7
        _signUpForm.value = SignUpFormState(
            usernameError =  if (!isUserNameValid) R.string.signup_invalid_username else null,
            passwordError = if (!isPasswordValid) R.string.signup_invalid_password else null,
            fullNameError = if (!isFullNameValid) R.string.signup_invalid_full_name else null,
            isDataValid = isFullNameValid && isUserNameValid && isPasswordValid
        )
    }

}