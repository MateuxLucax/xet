package com.xet.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xet.R
import com.xet.data.Result
import com.xet.domain.model.Contact
import com.xet.domain.model.FriendshipStatus
import com.xet.domain.usecase.friend.FriendUseCases
import com.xet.domain.usecase.search.SearchUseCases
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCases: SearchUseCases,
    private val friendUseCases: FriendUseCases,
): ViewModel() {

    private val page = 1

    private var currentUserToken: String = ""

    fun setCurrentUserToken(userToken: String) {
        currentUserToken = userToken
    }

    fun onInviteSent(fn: () -> Unit) {
        inviteSentCallback = fn
    }

    private val _searchResult = MutableLiveData<SearchResult>()
    val searchResult: LiveData<SearchResult> = _searchResult

    private val _updateInviteResult = MutableLiveData<UpdateInviteResult>()
    val updateInviteResult: LiveData<UpdateInviteResult> = _updateInviteResult

    private var inviteSentCallback: (() -> Unit)? = null

    fun search(query: String) {
        viewModelScope.launch {
            val result = searchUseCases.getUsers(currentUserToken, query, page)

            if (result is Result.Success) {
                if (result.data.isEmpty()) {
                    _searchResult.value = SearchResult(empty = R.string.search_list_empty)
                }
                _searchResult.value = SearchResult(success = result.data)
            } else {
                _searchResult.value = SearchResult(error = R.string.search_list_error)
            }
        }
    }

    fun sendInvite(contact: Contact) {
        viewModelScope.launch {
            val result = friendUseCases.sendInvite(currentUserToken, contact.userId)

            if (result is Result.Success) {
                _updateInviteResult.value = UpdateInviteResult(success = R.string.search_invite_sent_successfully)
                _searchResult.value?.success?.find{ it.userId == contact.userId }?.let {
                    it.friendshipStatus = FriendshipStatus.RECEIVED_FRIEND_REQUEST
                    inviteSentCallback?.invoke()
                }
            } else {
                _updateInviteResult.value = UpdateInviteResult(error = R.string.search_invite_sent_fail)
            }

        }
    }
}