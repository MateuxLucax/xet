package com.xet.domain.usecase.login

import com.xet.data.repository.login.ILoginRepository

class DoLogout (private val repository: ILoginRepository) {

    suspend operator fun invoke(): Boolean {
        return repository.logout()
    }

}