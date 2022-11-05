package com.xet.domain.usecase.friend

import com.xet.data.Result
import com.xet.data.repository.friend.IFriendRepository

class DoUpdateInvite(
    private val repository: IFriendRepository
) {

    suspend operator fun invoke(userFrom: String, tokenUserTo: String, accepted: Boolean): Result<Boolean> {
        return repository.updateInvite(userFrom, tokenUserTo, accepted)
    }

}