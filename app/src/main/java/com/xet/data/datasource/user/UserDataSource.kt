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

    private data class SignInRequestData(
        val username: String,
        val password: String
    ) {}

    private data class SignInOkResponseData(
        val token: String,
        val id: Long,
        val fullname: String
    ) {}

    override suspend fun signIn(username: String, password: String): LoggedUser {
        val dto = SignInRequestData(username, password)
        val request = jsonRequest("create-session", dto)
        return fetchDSD(request) { response ->
            if (!response.ok) throw exceptionFrom(response)
            val (token, id, fullname) = response.parseJSON(SignInOkResponseData::class.java)
            LoggedUser(id.toString(), fullname, username, token, password)
        }
    }

    private data class SignUpRequest(val username: String, val password: String, val fullname: String) {}
    private data class SignUpOkResponse(val id: Long) {}

    override suspend fun signUp(fullName: String, username: String, password: String): User {
        val dto = SignUpRequest(username, password, fullName)
        val req = jsonRequest("create-user", dto)
        return fetchDSD(req) { res ->
            if (!res.ok) throw exceptionFrom(res)
            val (id) = res.parseJSON(SignUpOkResponse::class.java)
            User(id.toString(), fullName, username)
        }
    }

    override suspend fun logout(token: String): Boolean {
        val request = emptyRequest("end-session", token)
        return fetchDSD(request) { okElseThrow(it) }
    }

    private data class UpdateProfileRequestData(
        val newFullname: String,
        val newPassword: String
    ) {}

    override suspend fun updateProfile(
        token: String,
        fullName: String,
        password: String
    ): Boolean {
        val dto = UpdateProfileRequestData(fullName, password)
        val request = jsonRequest("edit-user", dto, token)
        return fetchDSD(request) { okElseThrow(it) }
    }
}