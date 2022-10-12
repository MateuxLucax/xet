package com.xet.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun search(query: String) {

    }

    fun sendInvite(userTo: String) {
        viewModelScope.launch {
            val result = friendUseCases.sendInvite(currentUserId, userTo)
        }
    }

}