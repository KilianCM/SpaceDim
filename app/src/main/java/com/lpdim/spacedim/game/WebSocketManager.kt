package com.lpdim.spacedim.game

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.lpdim.spacedim.game.model.Event
import okhttp3.*
import okio.ByteString
import timber.log.Timber
import java.lang.Exception

class WebSocketLiveData : LiveData<Event>() {

    companion object {
        var instance = WebSocketLiveData()
        var webSocket: WebSocket? = null
        var client = OkHttpClient()
    }

    private val eventJsonAdapter = MoshiService.moshi.adapter(Event::class.java)

    override fun onInactive() {
        super.onInactive()
        disconnect()
    }

    private fun disconnect() {
        if(!hasActiveObservers()) {
            webSocket?.close(1000, "Game ended")
        }
    }

    fun connect(roomName: String, userId: Int) {
        val url = "ws://vps769278.ovh.net:8081/ws/join/$roomName/$userId"
        Timber.d("Try to connect to $url")
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, listener)

        client.dispatcher.executorService.shutdown()
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in Event>) {
        super.observe(owner, observer)
        Timber.d("Observing")
    }

    private val listener = object: WebSocketListener() {

        private val NORMAL_CLOSURE_STATUS = 1000

        override fun onOpen(webSocket: WebSocket, response: Response) {
            //TODO: remove this and add a button to be ready
            //webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !")
            Timber.d("Connection OK")

        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            processEvent(text)
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
    }

    private fun processEvent(value: String) {
        try {
            val event = eventJsonAdapter.fromJson(value)
            postValue(event)
        } catch (e: Exception) {
            Timber.e("Invalid event model")
        }
    }

}
