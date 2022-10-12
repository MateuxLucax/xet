package com.xet.data.repository.contact

import com.xet.data.Result
import com.xet.data.datasource.contact.IContactDataSource
import com.xet.domain.model.Contact

class ContactRepository(
    private val dataSource: IContactDataSource
): IContactRepository {

    override suspend fun getContacts(userId: String): Result<List<Contact>> {
        return dataSource.getContacts(userId)
    }

}