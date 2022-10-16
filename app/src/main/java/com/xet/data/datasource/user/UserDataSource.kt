package com.xet.data.datasource.user

import android.util.Log
import com.xet.domain.model.User
import com.xet.dsd.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

private const val TAG = "UserDataSource"

class UserDataSource: IUserDataSource {

    init {
        Log.i(TAG, "Initialized")
    }

    override suspend fun signIn(username: String, password: String): User {
        TODO("Not yet implemented")
    }

    data class SignUpRequest(val username: String, val password: String) {}
    data class SignUpOkResponse(val id: Long) {}
    data class SignUpErrResponse(val messageCode: String) {}

    override suspend fun signUp(fullName: String, username: String, password: String): User {
        val dto = SignUpRequest(username, password)
        val req = jsonRequest("create-user", dto)

        Log.i(TAG, "Making signUp request")

        return withContext(Dispatchers.IO) {
            Socket(DSD_HOST, DSD_PORT).use { socket ->
                Log.i(TAG, "Connected, sending request")
                val res = req.sendAndRead(socket)
                Log.i(TAG, "Sent request")
                Log.i(TAG, "${if (res.ok) "ok" else "err:"}${res.errKind}")

                if (res.ok) {
                    val (id) = res.readJson(SignUpOkResponse::class.java)
                    Log.i(TAG, id.toString())

                    // TODO fix mismatch:
                    //  - server has no "display name"
                    //  - server uses int IDs, client using string IDs
                    
                    User(id.toString(), username, username)
                } else {
                    val (codeString) = res.readJson(SignUpErrResponse::class.java)
                    val code = errCodeFrom(codeString)
                    Log.i(TAG, code.toString())
                    throw if (code != null) ErrCodeException(code) else Exception()
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