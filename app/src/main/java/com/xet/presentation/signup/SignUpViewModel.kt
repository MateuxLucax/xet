package com.xet.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xet.R
import com.xet.data.Result
import com.xet.domain.usecase.user.UserUseCases
import com.xet.dsd.ErrCodeException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val useCases: UserUseCases,
): ViewModel() {
    private lateinit var scope: Job
    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult

    fun signUp(fullName: String, username: String,password: String) {
        scope = viewModelScope.launch {
            val result = useCases.doSignUp(fullName, username, password)

            _signUpResult.value = when (result) {
                is Result.Success -> SignUpResult(success = result.data)
                is Result.Error -> SignUpResult(error = when (result.exception) {
                    is ErrCodeException -> result.exception.code.resource
                    else -> R.string.signup_failed_error
                })
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

    fun getScope(): Job {
        return this.scope
    }
}