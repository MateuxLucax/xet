package com.xet.dsd

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Socket

const val DSD_HOST = "45.77.148.79"
const val DSD_PORT = 8080

suspend fun <R> fetchDSD(request: Request, block: (Response) -> R): R {
    return withContext(Dispatchers.IO) {
        Socket(DSD_HOST, DSD_PORT).use {
            val response = request.sendAndRead(it)
            block(response)
        }
    }
}

fun exceptionFrom(response: Response): Exception {
    val (messageCode) = response.parseJSON(MessageCodeBody::class.java)
    return exceptionFrom(errCodeFrom(messageCode))
}

fun okElseThrow(response: Response): Boolean {
    return if (response.ok) true else throw exceptionFrom(response)
}

private var theLiveThread: LiveSocketListener? = null

fun setTheLiveThread(x: LiveSocketListener?) {
    theLiveThread = x
}

fun theLiveThread(): LiveSocketListener? {
    return theLiveThread
}