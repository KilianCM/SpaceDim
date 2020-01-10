package com.lpdim.spacedim.game.finish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import com.lpdim.spacedim.R
import com.lpdim.spacedim.databinding.FragmentFinishBinding


class FinishFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFinishBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_finish, container, false)

        binding.buttonPlayAgain.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_finishFragment_to_waitingRoomFragment)
        }

        return binding.root
    }

}
