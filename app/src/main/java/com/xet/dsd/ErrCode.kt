package com.xet.dsd

import androidx.annotation.StringRes
import com.xet.R

fun errCodeFrom(code: String): ErrCode? {
    return ErrCode.values().find{ it.name == code }
}

enum class ErrCode(@StringRes val resource: Int) {
    // general
    INTERNAL(R.string.errcode_placeholder),
    FAILED_TO_PARSE_JSON(R.string.errcode_placeholder),
    NO_USER_WITH_GIVEN_ID(R.string.errcode_placeholder),
    UNKNOWN_OPERATION(R.string.errcode_placeholder),
    TOKEN_EXPIRED(R.string.errcode_placeholder),
    MALFORMED_REQUEST(R.string.errcode_placeholder),
    NO_RESPONSE(R.string.errcode_placeholder),

    // create-session
    FAILED_TO_CREATE_USER(R.string.errcode_placeholder),
    USERNAME_IN_USE(R.string.username_in_use),

    // create-user
    INCORRECT_CREDENTIALS(R.string.errcode_placeholder),

    // search-users
    INVALID_PAGE_NUMBER(R.string.errcode_placeholder),

    // send-friend-request
    FRIEND_REQUEST_TO_YOURSELF(R.string.errcode_placeholder),
    YOU_ALREADY_SENT_FRIEND_REQUEST(R.string.errcode_placeholder),
    THEY_ALREADY_SENT_FRIEND_REQUEST(R.string.errcode_placeholder),
    FAILED_TO_SEND_FRIEND_REQUEST(R.string.errcode_placeholder),
    SENT_FRIEND_REQUEST_SUCCESSFULLY(R.string.errcode_placeholder),
    ;
}