package com.lpdim.spacedim.game

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }

    override fun onNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }
}
