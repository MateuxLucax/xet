package com.xet.domain.usecase.friend

import com.xet.data.Result
import com.xet.data.repository.friend.IFriendRepository
import com.xet.domain.model.Friend

class GetFriends(
    private val repository: IFriendRepository
) {

    suspend operator fun invoke(userId: String): Result<List<Friend>> {
        return repository.getFriends(userId)
    }

}