package com.xet.domain.usecase.user

import com.xet.data.Result
import com.xet.data.repository.user.IUserRepository
import com.xet.domain.model.User

class GetLoggedInUser (
    private val repository: IUserRepository
) {

    operator fun invoke(): Result<User> {
        val user = repository.loggedInUser
        return if (user != null) {
            Result.Success(user)
        } else {
            Result.Error(RuntimeException("User not set"))
        }
    }

}