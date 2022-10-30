package com.xet.data.repository.search

import com.xet.data.Result
import com.xet.domain.model.Contact

interface ISearchRepository {

    suspend fun search(token: String, query: String, page: Int): Result<List<Contact>>

}