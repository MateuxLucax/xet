package com.xet.domain.usecase.search

import com.xet.data.Result
import com.xet.data.repository.search.ISearchRepository
import com.xet.domain.model.Contact

class GetUsers(
    private val repository: ISearchRepository
) {

    suspend operator fun invoke(userId: String, query: String, offset: Number, limit: Number): Result<List<Contact>> {
        return repository.search(userId, query, offset, limit)
    }

}