package com.xet.data.repository.search

import com.xet.data.Result
import com.xet.domain.model.Contact

interface ISearchRepository {

    suspend fun search(userId: String, query: String, offset: Number, limit: Number): Result<List<Contact>>

}