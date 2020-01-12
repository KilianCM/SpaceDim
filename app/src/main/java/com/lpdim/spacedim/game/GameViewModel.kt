package com.lpdim.spacedim.game

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val _event = WebSocketLiveData.instance

    val event: WebSocketLiveData
        get() {
            return _event
        }
}