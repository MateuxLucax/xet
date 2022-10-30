package com.xet.domain.usecase.user

import com.xet.data.Result
import com.xet.data.repository.user.IUserRepository

class DoUpdateProfile(
    private val repository: IUserRepository
) {

    suspend operator fun invoke(fullName: String, password: String): Result<Boolean> {
        return repository.updateProfile(fullName, password)
    }

}