package com.xet.presentation.home

import androidx.lifecycle.ViewModel
import com.xet.R
import com.xet.data.Result
import com.xet.domain.usecase.user.LoginUseCases

class HomeViewModel(
    private val useCases: LoginUseCases
): ViewModel() {

    fun loadUser(): String {
        val result = useCases.loggedInUser()

        return if (result is Result.Success) {
            result.data.displayName
        } else {
            R.string.home_error.toString()
        }
    }

}