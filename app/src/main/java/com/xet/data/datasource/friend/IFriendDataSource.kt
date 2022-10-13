package com.xet.data.datasource.friend

import com.xet.domain.model.Contact
import com.xet.domain.model.Friend

interface IFriendDataSource {

    @Throws(Exception::class)
    suspend fun getFriends(userId: String): List<Friend>

    @Throws(Exception::class)
    suspend fun sendInvite(userFrom: String, userTo: String): Boolean

    @Throws(Exception::class)
    suspend fun getInvites(userId: String): List<Contact>

    @Throws(Exception::class)
    suspend fun updateInvite(userFrom: String, userTo: String, accepted: Boolean): Boolean

}