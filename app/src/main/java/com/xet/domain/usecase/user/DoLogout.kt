package com.xet.domain.usecase.user

import com.xet.data.repository.user.IUserRepository

class DoLogout (
    private val repository: IUserRepository
) {

    suspend operator fun invoke(): Boolean {
        return repository.logout()
    }

}