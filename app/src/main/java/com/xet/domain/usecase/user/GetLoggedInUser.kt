package com.xet.domain.usecase.user

import com.xet.data.Result
import com.xet.data.repository.user.IUserRepository
import com.xet.domain.model.LoggedUser

class GetLoggedInUser (
    private val repository: IUserRepository
) {

    operator fun invoke(): Result<LoggedUser> {
        return repository.getLoggedInUser()
    }
}