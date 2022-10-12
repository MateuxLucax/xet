package com.xet.data.datasource.contact

import com.xet.data.Result
import com.xet.domain.model.Contact

interface IContactDataSource {

    suspend fun getContacts(userId: String): Result<List<Contact>>

}