package com.xet.domain.usecase.friend

import com.xet.data.Result
import com.xet.data.repository.friend.IFriendRepository
import com.xet.domain.model.Contact

class GetInvites(
    private val repository: IFriendRepository
) {

    suspend operator fun invoke(userId: String): Result<List<Contact>> {
        return repository.getInvites(userId)
    }

}