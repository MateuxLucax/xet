package com.xet.data.repository.friend

import com.xet.data.Result
import com.xet.data.datasource.friend.IFriendDataSource
import com.xet.domain.model.Friend

class FriendRepository(
    private val dataSource: IFriendDataSource
): IFriendRepository {

    override suspend fun getFriends(userId: String): Result<List<Friend>> {
        return try {
            val result =  dataSource.getFriends(userId)

            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun sendInvite(userFrom: String, userTo: String): Result<Boolean> {
        return try {
            val result =  dataSource.sendInvite(userFrom, userTo)

            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}