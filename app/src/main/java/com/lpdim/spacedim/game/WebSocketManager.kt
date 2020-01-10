package com.lpdim.spacedim.game

import android.util.Log
import okhttp3.*
import okio.ByteString
import timber.log.Timber

class WebSocketManager : WebSocketListener() {
    private val NORMAL_CLOSURE_STATUS = 1000


    override fun onOpen(webSocket: WebSocket, response: Response) {

        //webSocket.send("{\"type\" :\"READY\", \"value\":true}")
        //webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !")
        Timber.d("Connection OK")


    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Timber.d("Receiving : $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Timber.d("Receiving bytes : %s", bytes.hex())
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        Timber.d("Closing : $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Timber.d("Error : %s", t.message)
    }

    companion object {

        fun joinRoom(roomName: String, id: Int) {
            val url = "ws://vps769278.ovh.net:8081/ws/join/$roomName/$id"

            Timber.d("Try to connect to $url")
            val request = Request.Builder().url(url).build()
            val listener = WebSocketManager()
            val client = OkHttpClient()

            client.newWebSocket(request, listener)
            client.dispatcher.executorService.shutdown()
        }
    }

}
