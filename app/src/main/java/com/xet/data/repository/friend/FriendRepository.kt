package com.xet.data.repository.friend

import com.xet.data.Result
import com.xet.data.datasource.friend.IFriendDataSource
import com.xet.domain.model.Contact
import com.xet.domain.model.Friend

class FriendRepository(
    private val dataSource: IFriendDataSource
): IFriendRepository {

    override suspend fun getFriends(userToken: String): Result<List<Friend>> {
        return try {
            val result =  dataSource.getFriends(userToken)

            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun sendInvite(tokenUserFrom: String, userTo: String): Result<Boolean> {
        return try {
            val result =  dataSource.sendInvite(tokenUserFrom, userTo)

            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getInvites(userToken: String): Result<List<Contact>> {
        return try {
            val result =  dataSource.getInvites(userToken)

            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateInvite(
        userFrom: String,
        userTo: String,
        accepted: Boolean
    ): Result<Boolean> {
        return try {
            val result =  dataSource.updateInvite(userFrom, userTo, accepted)

            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}