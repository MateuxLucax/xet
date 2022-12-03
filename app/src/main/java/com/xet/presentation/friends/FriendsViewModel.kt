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

    // FIXME! each time we open the fragment we get the contacts from getFriends(),
    //  which always returns them offline
    //  so we need to additionally need to keep a separate list of online users that gets queried on getContacts

    // FIXME making a user online from online-user-list does not seem to be working

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
        Log.v(TAG, "Got live message $m, now handling it")
        _friendsResult.value?.success?.let { friends ->
            try {
                JsonParser.parseString(m)?.asJsonObject?.let { json ->
                    val type = json["type"].asJsonPrimitive.asString
                    when (type) {
                        "online-user-list" -> {
                            Log.i(TAG, "Got a ONLINE USER LIST message")
                            val onlineIDs = json["userIDs"].asJsonArray.map{ id -> id.asJsonPrimitive.asString }.toSet()
                            for (friend in friends) {
                                val online = friend.userId in onlineIDs
                                Log.v(TAG, "friend $friend.username is ${if (online) "ONline" else "OFFline"}")
                                friend.status = statusFrom(online)
                            }
                        }
                        // using find is slower than if the users were in a Map<String, Friend> but whatever
                        "user-online" -> {
                            Log.i(TAG, "Got a USER ONLINE message")
                            val onlineUserID = json["userID"].asJsonPrimitive.asString
                            friends.find{ it.userId == onlineUserID }?.status = Status.ONLINE
                        }
                        "user-offline" -> {
                            Log.i(TAG, "Got a USER ONLINE message")
                            val offlineUserID = json["userID"].asJsonPrimitive.asString
                            friends.find{ it.userId == offlineUserID }?.status = Status.OFFLINE
                        }
                        else -> {}
                    }

                    // This works but idk if it's the right way to do it
                    _friendsResult.value?.copy().let {
                        _friendsResult.postValue(it)
                    }

                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception when parsing JSON message: $e")
            }

        }

    }

}