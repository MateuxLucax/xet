package com.xet.dsd

import android.util.Log
import java.io.IOException
import java.net.Socket

private const val TAG = "LiveSocketListener"

class LiveSocketListener(private val token: String): Thread() {

    private var handler: ((String) -> Unit)? = null
    private var goOffline: Boolean = false

    fun attachHandler(handler: (String) -> Unit) {
        this.handler = handler
    }

    fun detachHandler() {
        this.handler = null
    }

    fun goOffline() {
        this.goOffline = true
    }

    @Throws(IOException::class)
    private fun connectAndListen() {

        // first, go online (connect)
        val socket = Socket(DSD_HOST, DSD_PORT)
        val request = emptyRequest("go-online", token)
        val response = request.sendAndRead(socket)

        // the server is responsible for closing the socket

        if (!response.ok) {
            Log.e(TAG, "Non-ok go-online response: $response")
            return
        }

        val input = socket.getInputStream()
        val output = socket.getOutputStream()

        // then listen
        val bufsiz = 8192
        val buf = ByteArray(bufsiz)
        var off = 0
        var readingSize = true
        var messageSize = 0
        while (!goOffline) {
            val c = input.read()
            if (c == -1) break

            if (off == bufsiz) {
                Log.e(TAG, "message size exceeded bufsiz!") // really shouldn't happen
                off = 0
            }
            if (readingSize) {
                if (c == ' '.code) {
                    messageSize = String(buf, 0, off).toInt()
                    off = 0
                    readingSize = false
                } else {
                    buf[off++] = c.toByte()
                }
            } else {
                buf[off++] = c.toByte()
                if (off == messageSize) {
                    val message = String(buf, 0, off)

                    // TODO parse message here
                    // TODO detect if is ping with message.asJsonObject["type"] etc.
                    // TODO pass the parsed message to invoke, not the string

                    Log.v(TAG, "Got live message $message")

                    val isPing = message.contains("\"type\": \"ping\"")
                    if (isPing) {
                        output.write("pong".toByteArray())
                    } else {
                        handler?.invoke(message)
                    }

                    readingSize = true
                    off = 0
                }
            }
        }
    }

    override fun run() {
        // Keep reconnecting
        while (!goOffline) {
            try {
                connectAndListen()
            } catch (e: IOException) {
                Log.e(TAG, "$e")
            }
        }

        // Go offline
        Log.v(TAG, "Sending go-offline request")
        Socket(DSD_HOST, DSD_PORT).use {
            emptyRequest("go-offline", token).sendAndRead(it)
        }
    }
}