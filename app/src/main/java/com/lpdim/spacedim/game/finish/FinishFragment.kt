package com.lpdim.spacedim.game.finish

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController

import com.lpdim.spacedim.R
import com.lpdim.spacedim.databinding.FragmentFinishBinding
import com.lpdim.spacedim.game.GameActivity
import com.lpdim.spacedim.game.GameViewModel
import com.lpdim.spacedim.utils.MoshiService.eventAdapter
import com.lpdim.spacedim.game.model.Event
import com.lpdim.spacedim.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_finish.*


class FinishFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFinishBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_finish, container, false)

        binding.buttonPlayAgain.setOnClickListener { view ->
            backToHomePage()
//            val navController = view.findNavController()
//            if (navController.currentDestination?.id == R.id.action_finishFragment_to_homeActivity) {
//                navController.navigate(R.id.action_finishFragment_to_homeActivity)
//            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val event = eventAdapter.fromJson(it.getString("gameOver")) as Event.GameOver
            updateUI(event)
        }
    }

    private fun updateUI(event: Event.GameOver) {
        textViewFinishLevel.text = "${resources.getString(R.string.level_finish)} ${event.level}"

        if(event.win) {
            textViewGameOver.text = resources.getString(R.string.win)
        } else {
            textViewGameOver.text = resources.getString(R.string.loose)
        }

        textViewScoreValue.text = event.score.toString()
    }

    private fun backToHomePage() {
        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
    }


}
