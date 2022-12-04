package com.xet.presentation.friends

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.xet.R
import com.xet.data.Result
import com.xet.domain.model.Status
import com.xet.domain.model.statusFrom
import com.xet.domain.usecase.friend.FriendUseCases
import kotlinx.coroutines.launch

private const val TAG = "FriendsViewModel"

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

    fun messageHandler(m: String) {
        _friendsResult.value?.success?.let { friends ->
            try {
                var doRefresh: Boolean
                JsonParser.parseString(m)?.asJsonObject?.let { json ->
                    doRefresh = true
                    val type = json["type"].asJsonPrimitive.asString
                    when (type) {
                        // using find is slower than if the users were in a Map<String, Friend> but whatever
                        "user-online" -> {
                            val id = json["userID"].asJsonPrimitive.asString
                            friends.find{ it.userId == id }?.status = Status.ONLINE
                        }
                        "user-offline" -> {
                            val id = json["userID"].asJsonPrimitive.asString
                            friends.find{ it.userId == id }?.status = Status.OFFLINE
                        }
                        else -> {
                            doRefresh = false
                        }
                    }

                    if (doRefresh) {
                        // This works but idk if it's the right way to do it
                        _friendsResult.value?.copy().let {
                            _friendsResult.postValue(it)
                        }
                    }

                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception when parsing JSON message: $e")
            }

        }

    }

}