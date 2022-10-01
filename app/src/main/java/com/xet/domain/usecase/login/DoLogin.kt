package com.xet.domain.usecase.login

import com.xet.data.Result
import com.xet.data.repository.login.ILoginRepository
import com.xet.domain.model.LoggedInUser

class DoLogin (private val repository: ILoginRepository) {

    suspend operator fun invoke(username: String, password: String): Result<LoggedInUser> {
        return repository.login(username, password)
    }

}