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
        val userId: Long,
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
        return withContext(Dispatchers.IO) {
            Socket(DSD_HOST, DSD_PORT).use {
                val response = request.sendAndRead(it)
                if (response.ok) {
                    val responseData = response.parseJSON(Array<SearchUserData>::class.java)
                    responseData.map{ user ->
                        val friendshipStatus = friendshipStatusFrom(user.friendshipStatus)
                        if (friendshipStatus != null) {
                            Contact(user.userId.toString(), user.fullname, user.username, friendshipStatus)
                        } else {
                            throw RuntimeException("Unknown friendship status: " + user.friendshipStatus)
                        }
                    }.toList()
                } else {
                    val (code) = response.parseJSON(MessageCodeBody::class.java)
                    throw exceptionFrom(errCodeFrom(code))
                }
            }
        }
    }
}