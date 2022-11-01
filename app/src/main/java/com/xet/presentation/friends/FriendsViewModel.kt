package com.xet.presentation.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xet.R
import com.xet.data.Result
import com.xet.domain.usecase.friend.FriendUseCases
import kotlinx.coroutines.launch

class FriendsViewModel(
    private val useCases: FriendUseCases,
): ViewModel() {

    private val _friendsResult = MutableLiveData<FriendsResult>()
    val friendsResult: LiveData<FriendsResult> = _friendsResult

    fun getContacts(userToken: String) {
        viewModelScope.launch {
            val result = useCases.getFriends(userToken)

            if (result is Result.Success) {
                if (result.data.isEmpty()) {
                    _friendsResult.value = FriendsResult(empty = R.string.friends_empty)
                } else {
                    _friendsResult.value = FriendsResult(success = result.data)
                }
            } else {
                _friendsResult.value = FriendsResult(error = R.string.friends_fail)
            }
        }
    }

}