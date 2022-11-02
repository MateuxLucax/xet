package com.xet.data.datasource.search

import com.xet.domain.model.Contact
import com.xet.domain.model.friendshipStatusFrom
import com.xet.dsd.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Socket
import kotlin.RuntimeException;

class SearchDataSource : ISearchDataSource {

    private data class SearchRequestData(
        val query: String,
        val page: Int,
    ) {}

    private data class SearchUserData(
        val id: Long,
        val username: String,
        val fullname: String,
        val friendshipStatus: String
    ) {}

    override suspend fun search(
        token: String,
        query: String,
        page: Int
    ): List<Contact> {
        val requestData = SearchRequestData(query, page)
        val request = jsonRequest("search-users", requestData, token)
        return fetchDSD(request) { response ->
            if (!response.ok) throw exceptionFrom(response);
            val responseData = response.parseJSON(Array<SearchUserData>::class.java)
            responseData.map{ user ->
                val friendshipStatus = friendshipStatusFrom(user.friendshipStatus)
                if (friendshipStatus != null) {
                    Contact(user.id.toString(), user.fullname, user.username, friendshipStatus)
                } else {
                    throw RuntimeException("Unknown friendship status: " + user.friendshipStatus)
                }
            }.toList()
        }
    }
}