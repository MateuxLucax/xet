package com.xet.data.repository.contact

import com.xet.data.Result
import com.xet.domain.model.Contact

interface IContactRepository {

    suspend fun getContacts(userId: String): Result<List<Contact>>

}