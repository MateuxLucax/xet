package com.xet.data.datasource.friend

import com.xet.domain.model.Friend

interface IFriendDataSource {

    @Throws(Exception::class)
    suspend fun getFriends(userId: String): List<Friend>

    @Throws(Exception::class)
    suspend fun sendInvite(userFrom: String, userTo: String): Boolean

}