package com.lpdim.spacedim.game.play


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.lpdim.spacedim.game.GameViewModel
import com.lpdim.spacedim.R
import com.lpdim.spacedim.databinding.FragmentPlayBinding
import com.lpdim.spacedim.game.model.Event
import timber.log.Timber

class PlayFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPlayBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_play, container, false)

        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        binding.buttonFinish.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_gameFragment_to_finishFragment)
        }

        viewModel.event.observe(this, Observer { event ->
            Timber.d(event.toString())
            observeEvent(event)
        })

        return binding.root
    }

    private fun observeEvent(event: Event) {
        // when() ...
        //TODO: switch to do custom action according to Event type
    }
}
