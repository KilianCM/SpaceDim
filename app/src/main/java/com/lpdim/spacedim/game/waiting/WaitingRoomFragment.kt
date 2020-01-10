package com.lpdim.spacedim.game.waiting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.lpdim.spacedim.R
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.lpdim.spacedim.databinding.FragmentWaitingRoomBinding

class WaitingRoomFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentWaitingRoomBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_waiting_room, container, false)

        binding.button.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_waitingRoomFragment_to_gameFragment)
        }

        return binding.root
    }
}