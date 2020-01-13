package com.lpdim.spacedim.game.waiting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.lpdim.spacedim.R
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.lpdim.spacedim.databinding.FragmentWaitingRoomBinding
import com.lpdim.spacedim.game.GameViewModel
import com.lpdim.spacedim.game.WebSocketLiveData.Companion.webSocket
import com.lpdim.spacedim.game.model.Event
import com.lpdim.spacedim.game.model.EventType
import com.lpdim.spacedim.game.model.UIElement
import kotlinx.android.synthetic.main.fragment_waiting_room.*
import timber.log.Timber

class WaitingRoomFragment : Fragment() {
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentWaitingRoomBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_waiting_room, container, false)

        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        //TODO: get extra of intent to replace hardcoded values
        viewModel.event.connect("test_germain", 87)

        binding.buttonReady.setOnClickListener { view : View ->
            webSocket?.send("{\"type\" :\"READY\", \"value\":true}")

        }

        binding.buttonDev.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_waitingRoomFragment_to_gameFragment)
        }

        viewModel.event.observe(this, Observer { event ->
            Timber.d(event.toString())
            observeEvent(event)
        })


        return binding.root
    }

    private fun finishGame() {
        view?.findNavController()?.navigate(R.id.action_waitingRoomFragment_to_gameFragment)
    }

    private fun castEvent(event: Event){
        var castedEvent: Event

        if(event is Event.WaitingForPlayer) {
            castedEvent = event as Event.WaitingForPlayer
            castedEvent.userList.forEach(){
                playerOne.text = ("${it.name} : ${it.state}")
                Timber.d("Le joueur: ${it.name} Etat : ${it.state}")
                // TODO create UI element
            }


        }
    }


    private fun observeEvent(event: Event) {
        Timber.d(event.toString())
        when(event.type) {
            EventType.WAITING_FOR_PLAYER -> castEvent(event)
            EventType.GAME_STARTED -> finishGame()
        }
    }
}