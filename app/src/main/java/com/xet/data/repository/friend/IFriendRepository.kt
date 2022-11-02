package com.xet.data.repository.friend

import com.xet.data.Result
import com.xet.domain.model.Contact
import com.xet.domain.model.Friend
import com.xet.domain.model.LoggedUser

interface IFriendRepository {

    suspend fun getFriends(userToken: String): Result<List<Friend>>

    suspend fun sendInvite(tokenUserFrom: String, userTo: String): Result<Boolean>

    suspend fun getInvites(user: LoggedUser): Result<List<Contact>>

    suspend fun updateInvite(userFrom: String, userTo: String, accepted: Boolean): Result<Boolean>

}