package com.xet.domain.usecase.contact

import com.xet.data.Result
import com.xet.data.repository.contact.IContactRepository
import com.xet.domain.model.Contact

class GetContacts(
    private val repository: IContactRepository
) {

    suspend operator fun invoke(userId: String): Result<List<Contact>> {
        return repository.getContacts(userId)
    }

}