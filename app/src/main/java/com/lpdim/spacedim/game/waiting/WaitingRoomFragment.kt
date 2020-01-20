package com.lpdim.spacedim.game.waiting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.lpdim.spacedim.R
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.lpdim.spacedim.databinding.FragmentWaitingRoomBinding
import com.lpdim.spacedim.game.GameViewModel
import com.lpdim.spacedim.game.MoshiService.eventAdapter
import com.lpdim.spacedim.game.WebSocketLiveData.Companion.webSocket
import com.lpdim.spacedim.game.model.Event
import com.lpdim.spacedim.game.model.EventType
import com.lpdim.spacedim.game.model.UIElement
import kotlinx.android.synthetic.main.fragment_waiting_room.*
import timber.log.Timber

class WaitingRoomFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private var viewModelAdapter: PlayerAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentWaitingRoomBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_waiting_room, container, false)

        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        val roomName = activity?.intent?.extras?.getString("roomName")
        val userId = activity?.intent?.extras?.getInt("userId")


        roomName?.let { roomName ->
            userId?.let { userId ->
                viewModel.event.connect(roomName, userId)
            }
        }

        binding.buttonReady.setOnClickListener { view : View ->
            val event = Event.Ready(true)
            webSocket?.send(eventAdapter.toJson(event))
        }

        viewModel.event.observe(this, Observer { event ->
            Timber.d(event.toString())
            observeEvent(event)
        })

        viewModel.userList.observe(this, Observer { list ->
            Timber.d(list.toString())
            viewModelAdapter?.players = list
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d(arguments.toString())
    }

    private fun launchGame(event: Event) {
        val bundle = bundleOf("gameStarted" to eventAdapter.toJson(event))
        view?.findNavController()?.navigate(R.id.action_waitingRoomFragment_to_gameFragment, bundle)
    }

    private fun castEvent(event: Event){
        if(event is Event.WaitingForPlayer) {
            event.userList.forEach {
                playerOne.text = ("${it.name} : ${it.state}")
                Timber.d("Le joueur: ${it.name} Etat : ${it.state}")
            }
        }
    }


    private fun observeEvent(event: Event) {
        Timber.d(event.toString())
        when(event.type) {
            EventType.WAITING_FOR_PLAYER -> castEvent(event)
            EventType.GAME_STARTED -> launchGame(event)
        }
    }
}