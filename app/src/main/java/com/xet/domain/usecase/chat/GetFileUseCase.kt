package com.xet.domain.usecase.chat

import com.xet.data.Result
import com.xet.data.repository.chat.IChatRepository
import com.xet.domain.model.Message

class GetFileUseCase(
    private val repository: IChatRepository
) {

    suspend operator fun invoke(fileReference: String): Result<ByteArray> {
        return repository.getFile(fileReference)
    }

}