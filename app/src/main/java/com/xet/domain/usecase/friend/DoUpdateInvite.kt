package com.xet.domain.usecase.friend

import com.xet.data.Result
import com.xet.data.repository.friend.IFriendRepository

class DoUpdateInvite(
    private val repository: IFriendRepository
) {

    suspend operator fun invoke(userFrom: String, userTo: String, accepted: Boolean): Result<Boolean> {
        return repository.updateInvite(userFrom, userTo, accepted)
    }

}