package com.xet.domain.usecase.user

import com.xet.data.repository.user.IUserRepository

class IsUserLoggedIn (
    private val repository: IUserRepository
) {

    operator fun invoke(): Boolean {
        return repository.isLoggedIn
    }
}