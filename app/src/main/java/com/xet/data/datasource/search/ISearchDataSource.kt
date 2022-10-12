package com.xet.data.datasource.search

import com.xet.domain.model.Contact

interface ISearchDataSource {

    @Throws(Exception::class)
    suspend fun search(userId: String, query: String, offset: Number, limit: Number): List<Contact>

}