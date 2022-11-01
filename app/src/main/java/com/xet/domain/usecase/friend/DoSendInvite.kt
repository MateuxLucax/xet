package com.xet.domain.usecase.friend

import com.xet.data.Result
import com.xet.data.repository.friend.IFriendRepository

class DoSendInvite(
    private val repository: IFriendRepository
) {

    suspend operator fun invoke(tokenUserFrom: String, userTo: String): Result<Boolean> {
        return repository.sendInvite(tokenUserFrom, userTo)
    }

}