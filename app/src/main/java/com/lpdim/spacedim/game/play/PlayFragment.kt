package com.lpdim.spacedim.game.play


import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.lpdim.spacedim.game.GameViewModel
import com.lpdim.spacedim.R
import com.lpdim.spacedim.databinding.FragmentPlayBinding
import com.lpdim.spacedim.game.MoshiService.eventAdapter
import com.lpdim.spacedim.game.WebSocketLiveData
import com.lpdim.spacedim.game.model.Event
import com.lpdim.spacedim.game.model.EventType
import com.lpdim.spacedim.game.model.UIElement
import com.lpdim.spacedim.game.model.UIType
import kotlinx.android.synthetic.main.fragment_play.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.ShakeDetector
import android.app.ActionBar
import androidx.navigation.Navigation


class PlayFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private var countDownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPlayBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_play, container, false)

        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        viewModel.event.observe(this, Observer { event ->
            Timber.d(event.toString())
            observeEvent(event)
        })

        viewModel.timer.observe(this, Observer { timer ->
            createAndStartTimer(timer)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            try {
                //get the GameStarted event from the WaitingRoomFragment to generate the first action buttons
                val event = eventAdapter.fromJson(it.getString("gameStarted")) as Event.GameStarted
                generateUI(event.uiElementList)
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e("Impossible to generate action buttons")
            }
        }
    }

    /**
     * Create the countdown timer for each action to do
     * @param timer the starting time
     */
    private fun createAndStartTimer(time: Long) {
        countDownTimer?.cancel()
        countDownTimer = object: CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val timerText = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished).toString() + " " + getString(R.string.seconds_left)
                try {
                    textViewTimer.text = timerText
                } catch(e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun onFinish() {  }
        }
        countDownTimer?.start()
    }

    /**
     * On GameOver event, navigate to FinishFragment passing the event as bundle
     * (to display score and reached level on FinishFragment)
     * @param event the GameOver event
     */
    private fun finishGame(event: Event.GameOver) {
        WebSocketLiveData.closeConnection()
        val bundle = bundleOf("gameOver" to eventAdapter.toJson(event))
        view?.findNavController()?.navigate(R.id.action_gameFragment_to_finishFragment, bundle)
    }

    /**
     * Dispatch to correct method according to the event type
     * @param event
     */
    private fun observeEvent(event: Event) {
         when(event.type) {
             EventType.NEXT_LEVEL -> nextLevel(event as Event.NextLevel)
             EventType.NEXT_ACTION -> updateAction(event as Event.NextAction)
             EventType.GAME_OVER -> finishGame(event as Event.GameOver)
             else -> return
         }
    }

    /**
     * Update the action sentence at each NextAction event
     * @param event the NextAction event to get the sentence
     */
    private fun updateAction(event: Event.NextAction) {
        textViewAction.text = event.action.sentence
    }

    /**
     * On each NextLevel event, launch the regeneration of the UI (buttons)
     * @param event the NextLevel event to get the UIElementList
     */
    private fun nextLevel(event: Event.NextLevel) {
        generateUI(event.uiElementList)
    }

    /**
     * Browse the UI Elements list to launch the generation
     * @param uiElements list of UI elements to generate
     */
    private fun generateUI(uiElements: List<UIElement>) {
        layoutUiElementRow1.removeAllViews()
        layoutUiElementRow2.removeAllViews()
        uiElements.forEachIndexed { index: Int, uiElement: UIElement ->
            generateViewComponent(index, uiElement)
        }
    }

    /**
     * Generate UI element (BUTTON or SWITCH) and add it to layout
     * @param index index to know on which row to display element
     * @param uiElement the element to generate
     */
    private fun generateViewComponent(index: Int, uiElement: UIElement) {

        // Detect if the element is a SHAKE element and create a listener
        if(uiElement.type == UIType.SHAKE) {
            createShakeListener(uiElement)
            return
        }

        // Determination of the row to display element
        var layout = layoutUiElementRow1
        if(index % 2 == 0) layout = layoutUiElementRow2

        // Create Button or Switch element
        val generatedElement: Button?
        when(uiElement.type) {
            UIType.BUTTON -> generatedElement = Button(activity)
            UIType.SWITCH -> generatedElement = Switch(activity)
            else -> return
        }

        // Configure element with uiElement info
        generatedElement.id = uiElement.id
        generatedElement.text = uiElement.content

        val params = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(2, 2, 2, 2)
        generatedElement.setLayoutParams(params)
        // Create a ClickListener to send PlayerAction on click on this element
        generatedElement.setOnClickListener {
            sendPlayerAction(uiElement)
        }

        // Add uiElement to the view
        layout.addView(generatedElement)
    }

    /**
     * Create and start a shakeListener. When a shake is detected, we send a PlayerAction event to server.
     * @param uiElement the UIType.SHAKE element to create
     */
    private fun createShakeListener(uiElement: UIElement) {
        val shakeListener = object : ShakeDetector.ShakeListener {
            override fun onShakeDetected() {
                sendPlayerAction(uiElement)
            }

            override fun onShakeStopped() { }
        }
        Sensey.getInstance().startShakeDetection(shakeListener)
    }

    /**
     * Send a PlayerAction event to websocket
     * @param uiElement the triggered element
     */
    private fun sendPlayerAction(uiElement: UIElement) {
        val playerAction = Event.PlayerAction(uiElement)
        val playerActionJson = eventAdapter.toJson(playerAction)

        WebSocketLiveData.webSocket?.send(playerActionJson)
    }
}
