package com.xet.data.repository.friend

import com.xet.data.Result
import com.xet.domain.model.Contact
import com.xet.domain.model.Friend

interface IFriendRepository {

    suspend fun getFriends(userId: String): Result<List<Friend>>

    suspend fun sendInvite(userFrom: String, userTo: String): Result<Boolean>

    suspend fun getInvites(userId: String): Result<List<Contact>>

    suspend fun updateInvite(userFrom: String, userTo: String, accepted: Boolean): Result<Boolean>

}