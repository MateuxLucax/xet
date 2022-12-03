package com.xet.data.datasource.friend

import com.xet.domain.model.*
import com.xet.dsd.*

class FriendDataSource : IFriendDataSource {

    private data class FriendData(
        val id: Long,
        val username: String,
        val fullname: String
    )

    override suspend fun getFriends(userToken: String): List<Friend> {
        val request = emptyRequest("get-friends", userToken)
        return fetchDSD(request) { response ->
            if (!response.ok) throw exceptionFrom(response)
            val friendArray = response.parseJSON(Array<FriendData>::class.java)
            friendArray.map{
                val status = Status.OFFLINE
                Friend(it.id.toString(), it.fullname, it.username, status)
            }.toList()
        }
    }

    private data class SendInviteRequest(val userId: Long)

    override suspend fun sendInvite(tokenUserFrom: String, userTo: String): Boolean {
        val body = SendInviteRequest(userId = userTo.toLong())
        val request = jsonRequest("send-friend-request", body, tokenUserFrom)
        return fetchDSD(request) { okElseThrow(it) }
    }

    private data class GetInvitesUserData(
        val id: Long,
        val username: String,
        val fullname: String
    )

    private data class GetInvitesInviteData(
        val from: GetInvitesUserData,
        val to: GetInvitesUserData,
        // TODO unused for now:
        val createdAt: String,
        val updatedAt: String?
    )

    override suspend fun getInvites(user: LoggedUser): List<Contact> {
        return fetchDSD(emptyRequest("get-friend-requests", user.token)) { response ->
            if (!response.ok) throw exceptionFrom(response)
            val invites = response.parseJSON(Array<GetInvitesInviteData>::class.java)
            invites.map{
                val imTheSender = user.userId == it.from.id.toString()
                val other = if (imTheSender) it.to else it.from
                val friendshipStatus = if (imTheSender)
                    FriendshipStatus.RECEIVED_FRIEND_REQUEST
                else FriendshipStatus.SENT_FRIEND_REQUEST
                Contact(other.id.toString(), other.fullname, other.username, friendshipStatus)
            }
        }
    }

    private data class UpdateInviteData(
        val senderId: Long,
        val accepted: Boolean
    )

    override suspend fun updateInvite(
        userFrom: String,
        tokenUserTo: String,
        accepted: Boolean
    ): Boolean {
        val body = UpdateInviteData(userFrom.toLong(), accepted)
        val request = jsonRequest("finish-friend-request", body, tokenUserTo)
        return fetchDSD(request) { okElseThrow(it) }
    }
}