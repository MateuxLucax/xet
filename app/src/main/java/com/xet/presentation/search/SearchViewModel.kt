package com.xet.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xet.R
import com.xet.data.Result
import com.xet.domain.usecase.friend.FriendUseCases
import com.xet.domain.usecase.search.SearchUseCases
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCases: SearchUseCases,
    private val friendUseCases: FriendUseCases
): ViewModel() {

    private val offset = 0
    private val limit = 20

    private var currentUserId: String = ""

    fun setCurrentUserId(userId: String) {
        currentUserId = userId
    }

    private val _searchResult = MutableLiveData<SearchResult>()
    val searchResult: LiveData<SearchResult> = _searchResult

    private val _inviteResult = MutableLiveData<InviteResult>()
    val inviteResult: LiveData<InviteResult> = _inviteResult

    fun search(query: String) {
        viewModelScope.launch {
            val result = searchUseCases.getUsers(currentUserId, query, offset, limit)

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

    fun sendInvite(userTo: String) {
        viewModelScope.launch {
            val result = friendUseCases.sendInvite(currentUserId, userTo)

            if (result is Result.Success) {
                _inviteResult.value = InviteResult(success = R.string.search_invite_sent_successfully)
            } else {
                _inviteResult.value = InviteResult(error = R.string.search_invite_sent_fail)
            }
        }
    }
}