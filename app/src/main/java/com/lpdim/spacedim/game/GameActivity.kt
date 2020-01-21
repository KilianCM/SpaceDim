package com.lpdim.spacedim.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.lpdim.spacedim.R

class GameActivity : AppCompatActivity() {
    private lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        WebSocketManager.closeConnection()
    }
}
