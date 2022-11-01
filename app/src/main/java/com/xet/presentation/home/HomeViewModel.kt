package com.xet.presentation.home

import androidx.lifecycle.ViewModel
import com.xet.R
import com.xet.data.Result
import com.xet.domain.usecase.user.UserUseCases

class HomeViewModel(
    private val useCases: UserUseCases
): ViewModel() {

    fun loadUser(): String {
        val result = useCases.loggedInUser()

        return if (result is Result.Success) {
            result.data.token
        } else {
            R.string.friends_user_error.toString()
        }
    }

}