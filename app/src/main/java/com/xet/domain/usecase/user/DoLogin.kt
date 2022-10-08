package com.xet.domain.usecase.user

import com.xet.data.Result
import com.xet.data.repository.user.IUserRepository
import com.xet.domain.model.User

class DoLogin (
    private val repository: IUserRepository
) {

    suspend operator fun invoke(username: String, password: String): Result<User> {
        return repository.login(username, password)
    }

}