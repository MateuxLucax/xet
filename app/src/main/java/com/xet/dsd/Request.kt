package com.xet.dsd

import android.util.Log
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.Socket
import java.util.*

private const val TAG = "Request"

fun jsonRequest(operation: String, body: Any, token: String? = null, headers: List<String>?): Request {
    headers?.toMutableList()?.add("token $token")
    return Request(operation, Gson().toJson(body).toByteArray(), headers)
}

fun jsonRequest(operation: String, body: Any, token: String? = null): Request =
    Request(operation, Gson().toJson(body).toByteArray(), listOf("token $token"))

fun emptyRequest(operation: String, token: String? = null): Request =
    Request(operation, byteArrayOf(), listOf("token $token"))

class Request(
    private val operation: String,
    private val body: ByteArray,
    private val headers: List<String>? = null,
) {
    @Throws(IOException::class)
    private fun send(socket: Socket) {
        Log.v(TAG, "send")

        val reqHeaders = ArrayList<String>(4)
        reqHeaders.add("operation $operation")
        reqHeaders.add("body-size ${body.size}")
        if (headers != null){
            reqHeaders.addAll(headers)
        }

        val beforeBody = (reqHeaders + listOf("")).joinToString("\n") + "\n"

        val buffer = ByteArrayOutputStream()
        buffer.write(beforeBody.toByteArray())
        buffer.write(body)

        Log.v(TAG, "  whole request:\n${String(buffer.toByteArray())}")

        val ostream = socket.getOutputStream()
        ostream.write(buffer.toByteArray())
    }

    @Throws(IOException::class)
    private fun readHeaders(socket: Socket): Map<String, String> {
        Log.v(TAG, "readHeaders")

        val headers = HashMap<String, String>()

        val buf = ByteArray(1024)
        var len = 0

        val istream = socket.getInputStream()
        while (true) {
            val c = istream.read()
            val eof = c == -1
            if (!eof && c.toChar() != '\n' && len < buf.size) {
                buf[len++] = c.toByte()
            } else {
                val line = String(buf, 0, len)

                // empty line separates headers and body
                if (line.isBlank()) break

                val split = line.indexOf(" ")

                val k = line.substring(0 until split).trim().lowercase(Locale.ROOT)
                val v = line.substring(split+1).trim()
                headers[k] = v

                Log.v(TAG, "  read header $k: $v")

                if (eof) break
                len = 0
            }
        }

        Log.v(TAG, "  finished reading headers")

        return headers
    }

    @Throws(IOException::class)
    private fun readBody(socket: Socket, size: Int): ByteArray {
        if (size == 0) {
            return byteArrayOf()
        }
        val istream = socket.getInputStream()
        val body = ByteArray(size)
        var off = 0
        while (off < size) {
            val c = istream.read()
            if (c == -1) break
            body[off++] = c.toByte()
        }

        Log.v(TAG, "readBody, size $size:\n${String(body)}")

        return body
    }

    @Throws(IOException::class)
    fun sendAndRead(socket: Socket): Response {
        Log.v(TAG, "sendAndRead")

        send(socket)
        val headers = readHeaders(socket)
        val size = headers["body-size"]?.toInt() ?: 0
        val body = readBody(socket, size)
        return Response(headers, body)
    }
}