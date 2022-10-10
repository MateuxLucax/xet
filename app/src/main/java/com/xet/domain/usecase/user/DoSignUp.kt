package com.xet.domain.usecase.user

import com.xet.data.Result
import com.xet.data.repository.user.IUserRepository
import com.xet.domain.model.User

class DoSignUp(
    private val repository: IUserRepository
) {

    suspend operator fun invoke(fullName: String, username: String, password: String): Result<User> {
        return repository.signUp(fullName, username, password)
    }

}