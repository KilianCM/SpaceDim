package com.lpdim.spacedim.game.play


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.lpdim.spacedim.R
import com.lpdim.spacedim.databinding.FragmentPlayBinding

class PlayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPlayBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_play, container, false)

        binding.buttonFinish.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_gameFragment_to_finishFragment)
        }

        return binding.root
    }
}
