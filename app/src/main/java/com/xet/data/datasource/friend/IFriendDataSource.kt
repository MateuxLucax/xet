package com.xet.data.datasource.friend

import com.xet.domain.model.Contact
import com.xet.domain.model.Friend
import com.xet.domain.model.LoggedUser

interface IFriendDataSource {

    @Throws(Exception::class)
    suspend fun getFriends(userToken: String): List<Friend>

    @Throws(Exception::class)
    suspend fun sendInvite(tokenUserFrom: String, userTo: String): Boolean

    @Throws(Exception::class)
    suspend fun getInvites(user: LoggedUser): List<Contact>

    @Throws(Exception::class)
    suspend fun updateInvite(userFrom: String, tokenUserTo: String, accepted: Boolean): Boolean

}