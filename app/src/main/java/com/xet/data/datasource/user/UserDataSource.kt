package com.xet.data.datasource.user

import android.util.Log
import com.xet.domain.model.LoggedUser
import com.xet.domain.model.User
import com.xet.dsd.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Socket

private const val TAG = "UserDataSource"

class UserDataSource: IUserDataSource {

    init {
        Log.i(TAG, "Initialized")
    }

    private data class SignInRequestData(val username: String, val password: String) {}
    private data class SignInOkResponseData(val token: String, val id: Long, val fullname: String) {}

    override suspend fun signIn(username: String, password: String): LoggedUser {
        val dto = SignInRequestData(username, password)
        val request = jsonRequest("create-session", dto)
        Log.i(TAG, "Making sign in request")
        Log.v(TAG, "DTO: $dto")
        Log.v(TAG, "Request body: ${String(request.body)}")

        return withContext(Dispatchers.IO) {
            Socket(DSD_HOST, DSD_PORT).use {
                val response = request.sendAndRead(it)
                if (response.ok) {
                    val (token, id, fullname) = response.parseJSON(SignInOkResponseData::class.java)
                    LoggedUser(id.toString(), fullname, username, token)
                } else {
                    val (messageCode) = response.parseJSON(MessageCodeBody::class.java)
                    throw exceptionFrom(errCodeFrom(messageCode))
                }
            }
        }
    }

    private data class SignUpRequest(val username: String, val password: String, val fullname: String) {}
    private data class SignUpOkResponse(val id: Long) {}

    override suspend fun signUp(fullName: String, username: String, password: String): User {
        val dto = SignUpRequest(username, password, fullName)
        val req = jsonRequest("create-user", dto)

        Log.i(TAG, "Making signUp request")

        return withContext(Dispatchers.IO) {
            Socket(DSD_HOST, DSD_PORT).use { socket ->
                Log.i(TAG, "Connected, sending request")
                val res = req.sendAndRead(socket)
                Log.i(TAG, "Sent request")
                Log.i(TAG, "${if (res.ok) "ok" else "err:"}${res.errKind}")

                if (res.ok) {
                    val (id) = res.parseJSON(SignUpOkResponse::class.java)
                    Log.i(TAG, id.toString())

                    User(id.toString(), fullName, username)
                } else {
                    val (messageCode) = res.parseJSON(MessageCodeBody::class.java)
                    Log.i(TAG, messageCode)
                    throw exceptionFrom(errCodeFrom(messageCode))
                }
            }
        }
    }

    override fun logout(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfile(
        userId: String,
        fullName: String,
        username: String
    ): Boolean {
        TODO("Not yet implemented")
    }
}