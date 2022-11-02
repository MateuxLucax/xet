package com.xet.domain.usecase.friend

import com.xet.data.Result
import com.xet.data.repository.friend.IFriendRepository
import com.xet.domain.model.Contact
import com.xet.domain.model.LoggedUser

class GetInvites(
    private val repository: IFriendRepository
) {

    suspend operator fun invoke(user: LoggedUser): Result<List<Contact>> {
        return repository.getInvites(user)
    }

}