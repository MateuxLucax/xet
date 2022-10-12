package com.xet.data.repository.friend

import com.xet.data.Result
import com.xet.domain.model.Friend

interface IFriendRepository {

    @Throws(Exception::class)
    suspend fun getFriends(userId: String): Result<List<Friend>>

    suspend fun sendInvite(userFrom: String, userTo: String): Result<Boolean>

}