package com.xet.data.datasource.search

import com.xet.domain.model.Contact

interface ISearchDataSource {

    @Throws(Exception::class)
    suspend fun search(token: String, query: String, page: Int): List<Contact>

}