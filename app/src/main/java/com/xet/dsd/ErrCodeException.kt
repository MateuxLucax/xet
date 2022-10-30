package com.xet.dsd

fun exceptionFrom(code: ErrCode?): Exception {
    return if (code != null) ErrCodeException(code) else Exception()
}

class ErrCodeException(val code: ErrCode): Exception() {
}