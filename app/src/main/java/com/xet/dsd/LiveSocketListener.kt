package com.xet.dsd

import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

private const val TAG = "LiveSocketListener"

class LiveSocketListener(private val token: String): Thread() {

    override fun run() {

        // first, go online
        val socket = Socket(DSD_HOST, DSD_PORT)
        val request = emptyRequest("go-online", token)
        val response = request.sendAndRead(socket)
        // TODO check response ok

        // TODO so the activity can do something with the message:
        //  setHandler((message: String) -> Unit)
        //  unsetHandler()

        // TODO eventually go offline

        val input = socket.getInputStream()

        // then listen
        val bufsiz = 8192
        val buf = ByteArray(bufsiz)
        var off = 0
        var readingSize = true
        var messageSize = 0
        while (true) {
            val c = input.read()
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
                    Log.v(TAG, "Got live message $message")
                    readingSize = true
                    off = 0
                }
            }
        }
    }
}