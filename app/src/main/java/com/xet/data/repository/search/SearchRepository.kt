package com.xet.data.repository.search

import com.xet.data.Result
import com.xet.data.datasource.search.ISearchDataSource
import com.xet.domain.model.Contact

class SearchRepository(
    private val dataSource: ISearchDataSource
): ISearchRepository {

    override suspend fun search(
        userId: String,
        query: String,
        offset: Number,
        limit: Number
    ): Result<List<Contact>> {
        return try {
            val result = dataSource.search(userId, query, offset, limit)

            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}