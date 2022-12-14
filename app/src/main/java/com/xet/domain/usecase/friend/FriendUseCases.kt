package com.xet.domain.usecase.friend

data class FriendUseCases(
    val getFriends: GetFriends,
    val sendInvite: DoSendInvite,
    val getInvites: GetInvites,
    val updateInvite: DoUpdateInvite
)