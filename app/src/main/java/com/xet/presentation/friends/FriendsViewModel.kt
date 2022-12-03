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

    // TODO maybe this could be a MutableLiveData, even if used only internally
    //  (set observer to update friendsResult on init {})
    // TODO also if we want to keep the clean arch thing going this would be a repository or model or something
    private val onlineUserIDs: MutableSet<String> = mutableSetOf()

    fun getContacts(userToken: String) {
        viewModelScope.launch {
            val result = useCases.getFriends(userToken)

            if (result is Result.Success) {
                if (result.data.isEmpty()) {
                    _friendsResult.value = FriendsResult(empty = R.string.friends_empty)
                } else {
                    _friendsResult.value = FriendsResult(success = result.data)
                    refreshStatuses(false)
                }
            } else {
                _friendsResult.value = FriendsResult(error = R.string.friends_fail)
            }
        }
    }

    fun messageHandler(m: String) {
        Log.v(TAG, "Got live message $m, now handling it")
        _friendsResult.value?.success?.let { friends ->
            try {
                var doRefresh: Boolean
                JsonParser.parseString(m)?.asJsonObject?.let { json ->
                    doRefresh = true
                    val type = json["type"].asJsonPrimitive.asString
                    when (type) {
                        "online-user-list" -> {
                            Log.i(TAG, "Got a ONLINE USER LIST message")
                            val ids = json["userIDs"].asJsonArray.map{ id -> id.asJsonPrimitive.asString }
                            onlineUserIDs.addAll(ids)
                        }
                        // using find is slower than if the users were in a Map<String, Friend> but whatever
                        "user-online" -> {
                            Log.i(TAG, "Got a USER ONLINE message")
                            val id = json["userID"].asJsonPrimitive.asString
                            onlineUserIDs.add(id)
                        }
                        "user-offline" -> {
                            Log.i(TAG, "Got a USER ONLINE message")
                            val id = json["userID"].asJsonPrimitive.asString
                            onlineUserIDs.remove(id)
                        }
                        else -> {
                            doRefresh = false
                        }
                    }

                    if (doRefresh) {
                        refreshStatuses(true)
                    }

                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception when parsing JSON message: $e")
            }

        }

    }

    private fun refreshStatuses(repaint: Boolean) {
        _friendsResult.value?.success?.let { friends ->
            for (friend in friends) {
                friend.status = statusFrom(friend.userId in onlineUserIDs)
            }
        }

        if (repaint) {
            // This works but idk if it's the right way to do it
            _friendsResult.value?.copy().let {
                _friendsResult.postValue(it)
            }
        }
    }

}