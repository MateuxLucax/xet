package com.xet.domain.usecase.chat

import com.xet.data.Result
import com.xet.data.repository.chat.IChatRepository

class GetFileUseCase(
    private val repository: IChatRepository
) {

    suspend operator fun invoke(fileReference: String): Result<ByteArray> {
        return repository.getFile(fileReference)
    }

}