package com.xet.domain.usecase.login

import com.xet.data.Result
import com.xet.data.repository.login.ILoginRepository
import com.xet.domain.model.LoggedInUser

class GetLoggedInUser (private val repository: ILoginRepository) {

    operator fun invoke(): Result<LoggedInUser> {
        val user = repository.loggedInUser
        return if (user != null) {
            Result.Success(user)
        } else {
            Result.Error(RuntimeException("User not set"))
        }
    }

}