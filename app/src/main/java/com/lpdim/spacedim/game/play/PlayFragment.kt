package com.lpdim.spacedim.game.play


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.lpdim.spacedim.game.GameViewModel
import com.lpdim.spacedim.R
import com.lpdim.spacedim.databinding.FragmentPlayBinding
import com.lpdim.spacedim.game.model.Event
import com.lpdim.spacedim.game.model.EventType
import com.lpdim.spacedim.game.model.UIElement
import com.lpdim.spacedim.game.model.UIType
import kotlinx.android.synthetic.main.fragment_play.*
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
            finishGame()
        }

        viewModel.event.observe(this, Observer { event ->
            Timber.d(event.toString())
            observeEvent(event)
        })

        return binding.root
    }

    private fun finishGame() {
        view?.findNavController()?.navigate(R.id.action_gameFragment_to_finishFragment)
    }

    private fun observeEvent(event: Event) {
         when(event.type) {
             EventType.GAME_STARTED, EventType.NEXT_LEVEL -> Timber.d("Start")//generateUI(event)
             EventType.NEXT_ACTION -> updateAction(event as Event.NextAction)
             EventType.GAME_OVER -> finishGame()
         }
    }

    private fun updateAction(event: Event.NextAction) {
        textViewAction.text = event.action.sentence
    }

    private fun generateUI(event: Event) {
        var castedEvent: Event? = null
        if(event is Event.GameStarted || event is Event.NextLevel) {
            castedEvent = event as Event.GameStarted
            castedEvent.uiElementList.forEachIndexed { index: Int, uiElement: UIElement ->
                //generateViewComponent(index, uiElement)
            }
        }
    }

    private fun generateViewComponent(index: Int, uiElement: UIElement) {
        var layout = layoutUiElementRow1
        if(index % 2 == 0) layout = layoutUiElementRow2

        Timber.d("Generating ui")

        val generatedElement: Button? = null
        when(uiElement.type) {
            UIType.BUTTON -> Button(activity)
            UIType.SWITCH -> Switch(activity)
            UIType.SHAKE -> Button(activity) //TODO
            else -> Button(activity) //Button by default
        }

        generatedElement?.id = uiElement.id
        generatedElement?.text = uiElement.content
        generatedElement?.setOnClickListener {
            //sendPlayerAction(id)
            Timber.d("Click on ${it.id}")
        }

        layout.addView(generatedElement)
    }
}
