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
import timber.log.Timber

class WaitingRoomFragment : Fragment() {
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentWaitingRoomBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_waiting_room, container, false)

        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        //TODO: get extra of intent to replace hardcoded values
        viewModel.event.connect("test_kilian", 87)

        binding.button.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_waitingRoomFragment_to_gameFragment)
        }

        viewModel.event.observe(this, Observer { event ->
            Timber.d(event.toString())
        })

        return binding.root
    }
}