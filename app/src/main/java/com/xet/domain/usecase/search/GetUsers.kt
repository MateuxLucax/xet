package com.xet.domain.usecase.search

import com.xet.data.Result
import com.xet.data.repository.search.ISearchRepository
import com.xet.domain.model.Contact

class GetUsers(
    private val repository: ISearchRepository
) {

    suspend operator fun invoke(token: String, query: String, page: Int): Result<List<Contact>> {
        return repository.search(token, query, page)
    }

}