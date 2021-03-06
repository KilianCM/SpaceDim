package com.lpdim.spacedim.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.lpdim.spacedim.game.model.Event
import com.lpdim.spacedim.game.model.User

class GameViewModel : ViewModel() {

    private val _event = WebSocketManager.event

    val event: LiveData<Event>
        get() {
            return _event
        }

    val userList = Transformations.map(event) {
        if(it is Event.WaitingForPlayer) {
            return@map it.userList
        } else {
            return@map emptyList<User>()
        }
    }

    val timer = Transformations.map(event) {
        if(it is Event.NextAction) {
            return@map it.action.time
        } else {
            return@map 0.toLong()
        }
    }

    private val _errorMessage = WebSocketManager.errorMessage

    val errorMessage: LiveData<String>
        get() {
            return _errorMessage
        }
}