package com.xet.dsd

import com.google.gson.Gson

class Response(val headers: Map<String, String>, val body: ByteArray) {

    val ok: Boolean
    val errKind: String

    init {
        val status = headers["status"]
        if (status == null) {
            ok = false
            errKind = "format"
        } else {
            ok = status.startsWith("ok")
            errKind = if (ok) ""  else {
                val split = status.indexOf(":")
                if (split == -1) "format" else status.substring(split+1)
            }
        }
    }

    fun <T : Any> readJson(type: Class<T>): T =
        Gson().fromJson(String(body), type)
}