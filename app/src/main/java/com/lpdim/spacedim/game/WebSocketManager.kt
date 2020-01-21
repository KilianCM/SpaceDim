package com.lpdim.spacedim.game

import android.provider.Settings.Global.getString
import androidx.lifecycle.*
import com.lpdim.spacedim.api.API
import com.lpdim.spacedim.utils.MoshiService.eventAdapter
import com.lpdim.spacedim.game.model.Event
import okhttp3.*
import okio.ByteString
import timber.log.Timber
import java.lang.Exception

object WebSocketManager {
    var webSocket: WebSocket? = null
    var okHttpClient = OkHttpClient()

    var event = MutableLiveData<Event>()
    var errorMessage = MutableLiveData<String>()

    /**
     * Close the websocket connection
     */
    fun closeConnection() {
        webSocket?.close(1000, "Game ended or stopped")
        webSocket = null
    }

    /**
     * Create the connection to the Websocket server
     * @param roomName the name of the room to join
     * @param user the id of the user to connect
     */
    fun connect(roomName: String, userId: Int) {
        val url = API.BASE_URL_WS + API.JOIN_ROOM + "$roomName/$userId"
        Timber.d("Try to connect to $url")
        val request = Request.Builder().url(url).build()
        webSocket = okHttpClient.newWebSocket(request, listener)
    }

    private val listener = object: WebSocketListener() {

        private val NORMAL_CLOSURE_STATUS = 1000

        override fun onOpen(webSocket: WebSocket, response: Response) {
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
            processErrorMessage("Impossible de rejoindre ce vaisseau !")
        }
    }

    /**
     * Deserialize the json of an event and update the value of the LiveData
     * @param value the json to deserialize
     */
    private fun processEvent(value: String) {
        try {
            Timber.d(value)
            event.postValue(eventAdapter.fromJson(value))
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.e("Invalid event model")
        }
    }

    private fun processErrorMessage(value: String){
        Timber.d(value)
        try {
            errorMessage.postValue(value)
        } catch (e:Exception){
            e.printStackTrace()
        }
    }
}